package de.keeeks.nucleo.modules.messaging;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import io.nats.client.Connection;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public interface NatsConnection {

    /**
     * Creates a new instance of a NatsConnection
     *
     * @param logger      The logger to use for logging
     * @param credentials The credentials to use for connecting to the nats server
     * @return A new instance of a NatsConnection
     */
    static NatsConnection create(Logger logger, NatsCredentials credentials) {
        return new NucleoNatsConnection(logger, credentials);
    }

    /**
     * Returns the native connection if it is connected
     *
     * @return The native connection if it is connected
     */
    Optional<Connection> connection();

    /**
     * Publishes a packet to the given channel
     *
     * <p>
     * If the connection is not connected, the packet will be added to a
     * backlog and sent when the connection is established
     * </p>
     *
     * @param channel The channel to publish the packet to
     * @param packet  The packet to publish
     */
    void publishPacket(String channel, Packet packet);

    /**
     * Publishes a packet to the given channel
     *
     * <p>
     * If the connection is not connected, the packet will be discarded
     * </p>
     *
     * @param channel The channel to publish the packet to
     * @param packet  The packet to publish
     */
    void forcePublishPacket(String channel, Packet packet);

    /**
     * Sends a request to the given channel and waits for a response
     *
     * @param channel             The channel to send the request to
     * @param packet              The packet to send
     * @param responsePacketClass The class of the response packet
     * @param <R>                 The type of the response packet
     * @return A future that will be completed when the response is received
     */
    <R extends Packet> CompletableFuture<R> request(
            String channel,
            Packet packet,
            Class<R> responsePacketClass
    );

    /**
     * Registers a packet listener for the given channel
     *
     * @param listener The listener to register
     */
    void registerPacketListener(PacketListener<?> listener);

    /**
     * Registers multiple packet listeners
     *
     * @param listeners The listeners to register
     */
    default void registerPacketListener(PacketListener<?>... listeners) {
        for (PacketListener<?> listener : listeners) {
            registerPacketListener(listener);
        }
    }

    /**
     * Closes the connection
     */
    void close();
}