package de.keeeks.nucleo.modules.messaging;

import de.keeeks.nucleo.modules.messaging.packet.*;
import io.nats.client.*;
import lombok.Getter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NatsConnection {
    private static final List<NatsConnection> connections = new ArrayList<>();

    private final List<PacketBacklogMeta> packetBacklog = new ArrayList<>();
    @Getter
    private final Map<String, List<PacketListener<?>>> packetListeners = new HashMap<>();
    private final Map<String, Dispatcher> dispatcherMap = new HashMap<>();
    private final Logger logger;

    private Connection connection;

    public NatsConnection(Logger logger, NatsCredentials credentials) {
        this.logger = logger;
        tryConnect(credentials);
        connections.add(this);
    }

    public void close() {
        connection().ifPresent(openConnection -> {
            for (Dispatcher dispatcher : dispatcherMap.values()) {
                openConnection.closeDispatcher(dispatcher);
            }
            try {
                openConnection.close();
                logger.info("Closed connection %s".formatted(
                        openConnection.getServerInfo().getClientId()
                ));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Optional<Connection> connection() {
        return Optional.ofNullable(connection).filter(
                connection1 -> connection1.getStatus().equals(Connection.Status.CONNECTED)
        );
    }

    @Deprecated
    public Optional<Connection> getConnection() {
        return Optional.ofNullable(connection);
    }

    private void tryConnect(NatsCredentials credentials) {
        try {
            var options = Options.builder()
                    .server("nats://%s:%s".formatted(
                            credentials.hostname(),
                            credentials.port()
                    ))
                    .userInfo(credentials.username(), credentials.password())
                    .maxReconnects(-1)
                    .errorListener(new ErrorListener() {
                        @Override
                        public void exceptionOccurred(Connection conn, Exception exp) {
                            logger.log(
                                    Level.SEVERE,
                                    "Nats connection error",
                                    exp
                            );
                        }
                    })
                    .build();
            connection = Nats.connect(options);
            logger.info("Nats connected to %s:%s".formatted(
                    credentials.hostname(),
                    credentials.port()
            ));
            sendBacklogPackets();
        } catch (IOException | InterruptedException e) {
            logger.log(
                    Level.SEVERE,
                    "Failed to connect to nats. Retrying in 5 seconds.",
                    e
            );
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(5));
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            tryConnect(credentials);
        }
    }

    public void registerPacketListener(PacketListener<?>... listeners) {
        for (PacketListener<?> packetListener : listeners) {
            Class<? extends PacketListener<?>> clazz = (Class<? extends PacketListener<?>>) packetListener.getClass();
            var listenerChannel = clazz.getAnnotation(ListenerChannel.class);
            packetListeners.computeIfAbsent(
                    listenerChannel.value(),
                    s -> new ArrayList<>()
            ).add(packetListener);
            registerDispatcher(listenerChannel.value());
        }
    }

    public <R extends Packet> CompletableFuture<R> request(
            String channel,
            Packet packet,
            Class<R> responsePacketClass
    ) {
        return connection.request(
                channel,
                packet.packetMeta().toBytes()
        ).thenApply(
                message -> {
                    message.ack();
                    return responsePacketClass.cast(parsePacketFromMessage(message));
                }
        );
    }

    private void sendBacklogPackets() {
        for (PacketBacklogMeta packetBacklogMeta : packetBacklog) {
            publishPacket(
                    packetBacklogMeta.channel(),
                    packetBacklogMeta.packet()
            );
        }
    }

    /**
     * Try's to send the packet. If no active connection is found, the packet WILL get backlogged.
     *
     * @param channel The channel name the packet will be sent to.
     * @param packet  The packet which should be sent.
     */
    public void publishPacket(String channel, Packet packet) {
        connection().ifPresentOrElse(
                connection -> {
                    try {
                        connection.publish(channel, packet.packetMeta().toBytes());
                    } catch (Throwable throwable) {
                        logger.log(
                                Level.SEVERE,
                                "Failed to publish packet",
                                throwable
                        );
                    }
                },
                () -> {
                    logger.info("No active connection found. Backlogging packet.");
                    packetBacklog.add(new PacketBacklogMeta(packet, channel));
                }
        );
    }

    /**
     * Try's to send the packet. If no active connection is found, the packet WILL NOT get backlogged.
     *
     * @param channel The channel name the packet will be sent to.
     * @param packet  The packet which should be sent.
     */
    public void forcePublishPacket(String channel, Packet packet) {
        connection().ifPresent(
                connection -> connection.publish(channel, packet.packetMeta().toBytes())
        );
    }

    private void registerDispatcher(String channelName) {
        dispatcherMap.computeIfAbsent(
                channelName,
                channel -> {
                    var dispatcher = connection.createDispatcher(message -> {
                        var packetListeners = new ArrayList<>(this.packetListeners.get(channel));

                        Packet packet = parsePacketFromMessage(message);

                        packetListeners
                                .stream()
                                .filter(packetListener -> packetListener.packetClass().isAssignableFrom(
                                        packet.getClass()
                                ))
                                .sorted(Comparator.comparingInt(PacketListener::priority))
                                .sorted(Comparator.comparingInt(value -> {
                                    Class<? extends PacketListener> clazz = value.getClass();
                                    if (clazz.isAnnotationPresent(Order.class)) {
                                        return clazz.getAnnotation(Order.class).value().priority();
                                    }
                                    return 0;
                                }))
                                .forEach(packetListener -> packetListener.receiveRaw(packet, message));
                    }).subscribe(channel);

                    logger.info(String.format("Dispatcher for channel %s subscribed.", channel));
                    return dispatcher;
                });
    }

    private Packet parsePacketFromMessage(Message message) {
        return PacketMeta.fromJson(new String(message.getData())).packet();
    }

    public static void closeAllConnections() {
        for (NatsConnection connection : connections()) {
            connection.close();
        }
    }

    public static List<NatsConnection> connections() {
        return List.copyOf(connections);
    }

    public static NatsConnection create(Logger logger, NatsCredentials credentials) {
        return new NatsConnection(logger, credentials);
    }

    public record PacketBacklogMeta(
            Packet packet,
            String channel
    ) {
    }
}