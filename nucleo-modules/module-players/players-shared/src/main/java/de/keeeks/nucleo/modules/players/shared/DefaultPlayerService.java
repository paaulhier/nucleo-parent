package de.keeeks.nucleo.modules.players.shared;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.database.sql.MysqlCredentials;
import de.keeeks.nucleo.modules.messaging.MessagingModule;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.players.api.*;
import de.keeeks.nucleo.modules.players.api.packet.*;
import de.keeeks.nucleo.modules.players.api.packet.message.NucleoOnlinePlayerMessagePacket;
import de.keeeks.nucleo.modules.players.shared.json.NucleoOnlinePlayerSerializer;
import de.keeeks.nucleo.modules.players.shared.json.NucleoPlayerSerializer;
import de.keeeks.nucleo.modules.players.shared.json.PropertyHolderSerializer;
import de.keeeks.nucleo.modules.players.shared.json.SkinSerializer;
import de.keeeks.nucleo.modules.players.shared.packet.listener.NucleoOnlinePlayerUpdatePacketListener;
import de.keeeks.nucleo.modules.players.shared.packet.listener.NucleoOnlinePlayersRequestPacketListener;
import de.keeeks.nucleo.modules.players.shared.packet.listener.NucleoPlayerInvalidatePacketListener;
import de.keeeks.nucleo.modules.players.shared.packet.listener.NucleoPlayerUpdatePacketListener;
import de.keeeks.nucleo.modules.players.shared.sql.PlayerRepository;
import net.kyori.adventure.text.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultPlayerService implements PlayerService {
    private static final Module playersModule = Module.module("players");
    private static final JsonConfiguration sqlConfiguration = JsonConfiguration.create(
            playersModule.dataFolder(),
            "mysql"
    );
    private final List<NucleoPlayer> players = new LinkedList<>();
    private final Logger logger = playersModule.logger();

    private final PlayerRepository playerRepository;
    private final NatsConnection natsConnection;

    private DefaultPlayerService() {
        this.playerRepository = new PlayerRepository(sqlConfiguration.loadObject(
                MysqlCredentials.class,
                MysqlCredentials.defaultCredentials()
        ));
        this.natsConnection = Module.module(MessagingModule.class).defaultNatsConnection();
        GsonBuilder.registerSerializer(
                new SkinSerializer(),
                new NucleoPlayerSerializer(),
                new PropertyHolderSerializer(),
                new NucleoOnlinePlayerSerializer()
        );

        this.natsConnection.request(
                CHANNEL,
                new NucleoOnlinePlayersRequestPacket(),
                NucleoOnlinePlayersResponsePacket.class
        ).whenComplete((nucleoOnlinePlayersResponsePacket, throwable) -> {
            if (throwable != null) {
                logger.log(
                        Level.SEVERE,
                        "Failed to request online players from the network",
                        throwable
                );
                return;
            }
            players.addAll(nucleoOnlinePlayersResponsePacket.onlinePlayers());
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
                version
        );
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
        return players.stream().filter(
                player -> player.uuid().equals(uuid)
        ).findFirst().or(
                () -> Optional.ofNullable(playerRepository.player(uuid))
        );
    }

    @Override
    public Optional<NucleoPlayer> player(String name) {
        return players.stream().filter(
                player -> player.name().equals(name)
        ).findFirst().or(
                () -> Optional.ofNullable(playerRepository.player(name))
        );
    }

    @Override
    public List<NucleoOnlinePlayer> onlinePlayers() {
        return List.copyOf(players.stream().filter(
                player -> player instanceof NucleoOnlinePlayer
        ).map(player -> (NucleoOnlinePlayer) player).toList());
    }

    @Override
    public void updateNetworkWide(NucleoPlayer nucleoPlayer) {
        natsConnection.publishPacket(
                CHANNEL,
                new NucleoPlayerUpdatePacket(nucleoPlayer)
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
        this.players.removeIf(
                player -> player.uuid().equals(nucleoPlayer.uuid())
        );
        this.players.add(nucleoPlayer);
        logger.info("Updated player " + nucleoPlayer.name() + " with UUID " + nucleoPlayer.uuid());
    }

    @Override
    public void updateCache(NucleoOnlinePlayer nucleoOnlinePlayer) {
        this.players.removeIf(
                player -> player.uuid().equals(nucleoOnlinePlayer.uuid())
        );
        this.players.add(nucleoOnlinePlayer);
        logger.info("Updated online player " + nucleoOnlinePlayer.name() + " with UUID " + nucleoOnlinePlayer.uuid());
    }

    @Override
    public void invalidateCache(UUID uuid) {
        if (this.players.removeIf(
                player -> player.uuid().equals(uuid)
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
        playerRepository.createOrUpdateSkin(
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
    public void connectPlayer(
            NucleoOnlinePlayer nucleoOnlinePlayer,
            String server,
            Consumer<Boolean> successCallback
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
                successCallback.accept(false);
                return;
            }
            successCallback.accept(nucleoOnlinePlayerConnectResponsePacket.success());
        });
    }

    public static DefaultPlayerService create() {
        return new DefaultPlayerService();
    }
}