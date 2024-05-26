package de.keeeks.nucleo.modules.players.shared;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.database.sql.MysqlCredentials;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.players.api.*;
import de.keeeks.nucleo.modules.players.api.packet.*;
import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerConnectResponsePacket.State;
import de.keeeks.nucleo.modules.players.api.packet.message.NucleoOnlinePlayerMessagePacket;
import de.keeeks.nucleo.modules.players.shared.json.CommentSerializer;
import de.keeeks.nucleo.modules.players.shared.json.NucleoOnlinePlayerSerializer;
import de.keeeks.nucleo.modules.players.shared.json.NucleoPlayerSerializer;
import de.keeeks.nucleo.modules.players.shared.json.PropertyHolderSerializer;
import de.keeeks.nucleo.modules.players.shared.packet.listener.NucleoOnlinePlayerUpdatePacketListener;
import de.keeeks.nucleo.modules.players.shared.packet.listener.NucleoOnlinePlayersRequestPacketListener;
import de.keeeks.nucleo.modules.players.shared.packet.listener.NucleoPlayerInvalidatePacketListener;
import de.keeeks.nucleo.modules.players.shared.packet.listener.NucleoPlayerUpdatePacketListener;
import de.keeeks.nucleo.modules.players.shared.sql.CommentRepository;
import de.keeeks.nucleo.modules.players.shared.sql.PlayerRepository;
import de.keeeks.nucleo.modules.players.shared.sql.PropertiesRepository;
import de.keeeks.nucleo.modules.players.shared.sql.SkinRepository;
import net.kyori.adventure.text.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DefaultPlayerService implements PlayerService {
    private static final Module playersModule = Module.module("players");
    private static final JsonConfiguration sqlConfiguration = JsonConfiguration.create(
            playersModule.dataFolder(),
            "mysql"
    );
    private final List<PlayerCacheElement<? extends NucleoPlayer>> playersCache = new LinkedList<>();
    private final Map<String, List<NucleoPlayer>> playersByIpAddress = new HashMap<>();
    private final List<UUID> playersSortedByPlayTime = new ArrayList<>();

    private final Logger logger = playersModule.logger();

    private final PlayerRepository playerRepository;
    private final SkinRepository skinRepository;
    private final NatsConnection natsConnection;

    private DefaultPlayerService() {
        MysqlConnection mysqlConnection = MysqlConnection.create(sqlConfiguration.loadObject(
                MysqlCredentials.class,
                MysqlCredentials.defaultCredentials()
        ));

        PropertiesRepository propertiesRepository = new PropertiesRepository(mysqlConnection);

        CommentRepository commentRepository = ServiceRegistry.registerService(
                CommentRepository.class,
                new CommentRepository(mysqlConnection)
        );
        this.skinRepository = new SkinRepository(mysqlConnection);
        this.playerRepository = new PlayerRepository(
                mysqlConnection,
                skinRepository,
                commentRepository,
                propertiesRepository
        );

        this.natsConnection = ServiceRegistry.service(NatsConnection.class);
        GsonBuilder.registerSerializer(
                new CommentSerializer(),
                new NucleoPlayerSerializer(),
                new PropertyHolderSerializer(),
                new NucleoOnlinePlayerSerializer()
        );

        this.natsConnection.request(
                CHANNEL,
                new NucleoOnlinePlayersRequestPacket(),
                NucleoOnlinePlayersResponsePacket.class
        ).whenComplete((nucleoOnlinePlayersResponsePacket, throwable) -> {
            if (throwable != null) return;

            for (NucleoOnlinePlayer nucleoOnlinePlayer : nucleoOnlinePlayersResponsePacket.onlinePlayers()) {
                PlayerCacheElement<NucleoOnlinePlayer> cacheElement = PlayerCacheElement.create(
                        nucleoOnlinePlayer,
                        false
                );
                playersCache.add(cacheElement);
            }
            logger.info("Received online players from the network (Count: %s)".formatted(
                    nucleoOnlinePlayersResponsePacket.onlinePlayers().size()
            ));
        });

        this.natsConnection.registerPacketListener(
                new NucleoPlayerInvalidatePacketListener(this),
                new NucleoPlayerUpdatePacketListener(this),
                new NucleoOnlinePlayerUpdatePacketListener(this),
                new NucleoOnlinePlayersRequestPacketListener(this)
        );

        Scheduler.runAsyncTimer(
                () -> List.copyOf(playersCache).stream().filter(
                        cacheElement -> cacheElement.expired(Duration.ofMinutes(30))
                ).forEach(cacheElement -> {
                    logger.info("Invalidating player %s with UUID %s. Reason: Expired".formatted(
                            cacheElement.player().name(),
                            cacheElement.player().uuid()
                    ));
                    invalidateCacheNetworkWide(cacheElement.player().uuid());
                }),
                0,
                1,
                TimeUnit.SECONDS
        );

        Scheduler.runAsyncTimer(
                () -> {
                    playersSortedByPlayTime.clear();
                    playersSortedByPlayTime.addAll(playerRepository.playersSortedByPlayTime());
                },
                0,
                10,
                TimeUnit.MINUTES
        );
    }

    private List<NucleoPlayer> players() {
        return List.copyOf(playersCache).stream().map(
                PlayerCacheElement::player
        ).collect(Collectors.toList());
    }

    @Override
    public NucleoPlayer createPlayer(UUID uuid, String name) {
        logger.info("Creating player " + name + " with UUID " + uuid);
        return playerRepository.createPlayer(uuid, name);
    }

    @Override
    public NucleoOnlinePlayer createOnlinePlayer(
            NucleoPlayer nucleoPlayer,
            String server,
            String proxy,
            String address,
            Version version
    ) {
        logger.info("Creating online player %s with UUID %s on proxy %s (Connected from: %s)".formatted(
                nucleoPlayer.name(),
                nucleoPlayer.uuid(),
                proxy,
                address
        ));
        return new DefaultNucleoOnlinePlayer(
                nucleoPlayer,
                proxy,
                server,
                address,
                version,
                OnlineState.ONLINE
        );
    }

    @Override
    public List<NucleoPlayer> players(String ipAddress) {
        return playersByIpAddress.computeIfAbsent(
                ipAddress,
                s -> {
                    List<NucleoPlayer> players = new ArrayList<>(onlinePlayers().stream().filter(
                            onlinePlayer -> onlinePlayer.ipAddress().equals(ipAddress)
                    ).toList());

                    for (NucleoPlayer player : playerRepository.players(ipAddress)) {
                        if (players.stream().anyMatch(nucleoPlayer -> nucleoPlayer.uuid().equals(player.uuid())))
                            continue;
                        players.add(player);
                    }
                    return List.copyOf(players);
                }
        );
    }

    @Override
    public void deletePlayer(UUID uuid) {
        playerRepository.deletePlayer(uuid);
    }

    @Override
    public void publishOnlinePlayerCreation(NucleoOnlinePlayer nucleoOnlinePlayer) {
        natsConnection.publishPacket(
                CHANNEL,
                new NucleoOnlinePlayerNetworkJoinPacket(nucleoOnlinePlayer)
        );
    }

    @Override
    public Optional<NucleoPlayer> player(UUID uuid) {
        return players().stream().filter(
                player -> player.uuid().equals(uuid)
        ).findFirst().or(() -> Optional.ofNullable(playerRepository.player(uuid)).map(nucleoPlayer -> {
            updateNetworkWide(nucleoPlayer);
            return nucleoPlayer;
        }));
    }

    @Override
    public Optional<NucleoPlayer> player(String name) {
        return players().stream().filter(
                player -> player.name().equals(name)
        ).findFirst().or(() -> players().stream().filter(
                player -> player.name().equalsIgnoreCase(name)
        ).findFirst()).or(() -> Optional.ofNullable(playerRepository.player(name)).map(nucleoPlayer -> {
            updateNetworkWide(nucleoPlayer);
            return nucleoPlayer;
        }));
    }

    @Override
    public List<NucleoOnlinePlayer> onlinePlayers() {
        return players().stream().filter(
                player -> player instanceof NucleoOnlinePlayer
        ).map(player -> (NucleoOnlinePlayer) player).toList();
    }

    @Override
    public List<NucleoPlayer> playersSortedByPlayTime() {
        return List.copyOf(playersSortedByPlayTime).stream().map(uuid ->
                player(uuid).orElse(null)
        ).filter(Objects::nonNull).toList();
    }

    @Override
    public void updateNetworkWide(NucleoPlayer nucleoPlayer) {
        if (nucleoPlayer instanceof NucleoOnlinePlayer onlinePlayer) {
            updateNetworkWide(onlinePlayer);
            return;
        }

        natsConnection.publishPacket(
                CHANNEL,
                new NucleoPlayerUpdatePacket(nucleoPlayer, Module.serviceName())
        );
    }

    @Override
    public void updateNetworkWide(NucleoOnlinePlayer nucleoOnlinePlayer) {
        natsConnection.publishPacket(
                CHANNEL,
                new NucleoOnlinePlayerUpdatePacket(nucleoOnlinePlayer)
        );
    }

    @Override
    public void updatePlayerName(UUID uuid, String newName) {
        player(uuid).ifPresent(
                nucleoPlayer -> {
                    NucleoPlayer updatedPlayer = nucleoPlayer.updateName(newName);
                    updateCache(updatedPlayer);
                    natsConnection.publishPacket(
                            CHANNEL,
                            new NucleoPlayerUpdateNamePacket(
                                    updatedPlayer,
                                    nucleoPlayer.name(),
                                    newName
                            )
                    );
                    savePlayerToDatabase(updatedPlayer);
                }
        );
    }

    @Override
    public void updateCache(NucleoPlayer nucleoPlayer) {
        if (nucleoPlayer instanceof NucleoOnlinePlayer) {
            updateCache((NucleoOnlinePlayer) nucleoPlayer);
            System.out.println("Tried to update cache with online player");
            return;
        }
        cacheLocal(nucleoPlayer, aBoolean -> {
            if (aBoolean) {
                logger.warning("Tried to update cache with online player");
            }
        });
        logger.info("Updated offline player " + nucleoPlayer.name() + " with UUID " + nucleoPlayer.uuid());
    }

    @Override
    public void updateCache(NucleoOnlinePlayer nucleoOnlinePlayer) {
        cacheLocal(nucleoOnlinePlayer, aBoolean -> {
            if (!aBoolean) logger.warning("Tried to update cache with offline player");
        });
        logger.info("Updated online player " + nucleoOnlinePlayer.name() + " with UUID " + nucleoOnlinePlayer.uuid());
    }

    private <T extends NucleoPlayer> void cacheLocal(T nucleoPlayer, Consumer<Boolean> consumer) {
        this.playersCache.removeIf(
                cacheElement -> cacheElement.player().uuid().equals(nucleoPlayer.uuid())
        );
        this.playersCache.add(PlayerCacheElement.create(
                nucleoPlayer,
                !(nucleoPlayer instanceof NucleoOnlinePlayer)
        ));
        consumer.accept(nucleoPlayer instanceof NucleoOnlinePlayer);
    }

    @Override
    public void invalidateCache(UUID uuid) {
        if (playersCache.removeIf(
                cacheElement -> cacheElement.player().uuid().equals(uuid)
        )) {
            logger.info("Invalidated player with UUID " + uuid);
        }
    }

    @Override
    public void invalidateCacheNetworkWide(UUID uuid) {
        natsConnection.publishPacket(
                CHANNEL,
                new NucleoPlayerInvalidatePacket(uuid)
        );
    }

    @Override
    public void savePlayerToDatabase(NucleoPlayer nucleoPlayer) {
        playerRepository.updatePlayerData(nucleoPlayer);
        skinRepository.createOrUpdateSkin(
                nucleoPlayer.uuid(),
                nucleoPlayer.skin().value(),
                nucleoPlayer.skin().signature()
        );
    }

    @Override
    public void send(UUID receiver, Component component, NucleoMessageSender.MessageType messageType) {
        natsConnection.publishPacket(
                CHANNEL,
                new NucleoOnlinePlayerMessagePacket(
                        receiver,
                        component,
                        messageType
                )
        );
    }

    @Override
    public void executeCommand(
            NucleoOnlinePlayer nucleoOnlinePlayer,
            CommandTarget commandTarget,
            String command
    ) {
        natsConnection.publishPacket(
                CHANNEL,
                new NucleoOnlinePlayerExecuteCommandPacket(
                        nucleoOnlinePlayer,
                        commandTarget,
                        command
                )
        );
    }

    @Override
    public void connectPlayer(
            NucleoOnlinePlayer nucleoOnlinePlayer,
            String server,
            Consumer<State> successCallback
    ) {
        natsConnection.request(
                CHANNEL,
                new NucleoOnlinePlayerConnectRequestPacket(
                        nucleoOnlinePlayer,
                        server
                ),
                NucleoOnlinePlayerConnectResponsePacket.class
        ).whenCompleteAsync((nucleoOnlinePlayerConnectResponsePacket, throwable) -> {
            if (throwable != null) {
                logger.log(
                        Level.SEVERE,
                        "Failed to connect player " + nucleoOnlinePlayer.name() + " to server " + server,
                        throwable
                );
            }
            successCallback.accept(nucleoOnlinePlayerConnectResponsePacket.state());
        });
    }

    @Override
    public void kickPlayer(NucleoOnlinePlayer nucleoOnlinePlayer, Component reason, boolean raw) {
        natsConnection.publishPacket(
                CHANNEL,
                new NucleoOnlinePlayerKickPacket(
                        nucleoOnlinePlayer,
                        reason,
                        raw
                )
        );
    }

    public static DefaultPlayerService create() {
        return new DefaultPlayerService();
    }
}