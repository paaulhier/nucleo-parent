package de.keeeks.nucleo.modules.players.api;

import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerConnectResponsePacket.State;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface PlayerService {

    String CHANNEL = "nucleo:players";

    /**
     * Creates a new player with the given UUID and name.
     *
     * @param uuid the UUID of the player
     * @param name the name of the player
     * @return the created player
     */
    NucleoPlayer createPlayer(UUID uuid, String name);

    /**
     * Creates a new online player with the given player, server, proxy and address.
     *
     * @param nucleoPlayer the player
     * @param server       the server the player is on
     * @param proxy        the proxy the player is on
     * @param address      the address of the player
     * @param version      the version of the player
     * @return the created online player
     */
    NucleoOnlinePlayer createOnlinePlayer(
            NucleoPlayer nucleoPlayer,
            String server,
            String proxy,
            String address,
            Version version
    );

    List<NucleoPlayer> players(String ipAddress);

    /**
     * Deletes the player with the given UUID.
     *
     * @param uuid the UUID of the player to delete
     */
    void deletePlayer(UUID uuid);

    /**
     * Publishes the creation of the given online player.
     *
     * @param nucleoOnlinePlayer the online player to publish the creation is of
     */
    void publishOnlinePlayerCreation(NucleoOnlinePlayer nucleoOnlinePlayer);

    /**
     * Returns the player with the given UUID.
     *
     * @param uuid the UUID of the player
     * @return the player with the given UUID, if present
     */
    Optional<NucleoPlayer> player(UUID uuid);

    /**
     * Returns the player with the given name.
     * The name is case-sensitive.
     *
     * @param name the name of the player
     * @return the player with the given name, if present
     */
    Optional<NucleoPlayer> player(String name);

    /**
     * Returns the online player with the given UUID.
     *
     * @param uuid the UUID of the player
     * @return the online player with the given UUID, if present
     */
    default Optional<NucleoOnlinePlayer> onlinePlayer(UUID uuid) {
        return onlinePlayers().stream()
                .filter(player -> player.uuid().equals(uuid))
                .findFirst();
    }

    /**
     * Returns the online player with the given name.
     * The name is case-sensitive.
     *
     * @param name the name of the player
     * @return the online player with the given name, if present
     */
    default Optional<NucleoOnlinePlayer> onlinePlayer(String name) {
        return onlinePlayers().stream()
                .filter(player -> player.name().equals(name))
                .findFirst()
                .or(() -> {
                    return onlinePlayers().stream()
                            .filter(player -> player.name().equalsIgnoreCase(name))
                            .findFirst();
                });
    }


    /**
     * Returns a list of all online players.
     *
     * @return a list of all online players
     */
    List<NucleoOnlinePlayer> onlinePlayers();

    /**
     * Returns a sorted list of all players by play time.
     * @return a sorted list of all players by play time
     */
    List<NucleoPlayer> playersSortedByPlayTime();

    /**
     * Returns a list of all online players on the given server.
     * @param server the server to get the online players from
     * @return a list of all online players on the given server
     */
    default List<NucleoOnlinePlayer> onlinePlayers(String server) {
        return onlinePlayers().stream()
                .filter(player -> player.server().equals(server))
                .toList();
    }

    /**
     * Updates the network-wide cache with the given player.
     *
     * @param nucleoPlayer the player to update the network-wide cache with
     */
    void updateNetworkWide(NucleoPlayer nucleoPlayer);

    /**
     * Updates the network-wide cache with the given player.
     *
     * @param nucleoOnlinePlayer the player to update the network-wide cache with
     */
    void updateNetworkWide(NucleoOnlinePlayer nucleoOnlinePlayer);

    /**
     * Broadcasts the name update to all services
     *
     * @param uuid    the UUID of the player
     * @param newName the new name of the player
     */
    void updatePlayerName(UUID uuid, String newName);

    /**
     * Updates the cache with the given player.
     *
     * @param nucleoPlayer the player to update the cache with
     */
    void updateCache(NucleoPlayer nucleoPlayer);

    /**
     * Updates the cache with the given player.
     *
     * @param nucleoOnlinePlayer the player to update the cache with
     */
    void updateCache(NucleoOnlinePlayer nucleoOnlinePlayer);

    /**
     * Invalidates the cache with the given UUID.
     *
     * @param uuid the UUID of the player to invalidate the cache with
     */
    void invalidateCache(UUID uuid);

    /**
     * Invalidates the network-wide cache with the given UUID.
     *
     * @param uuid the UUID of the player to invalidate the network-wide cache with
     */
    void invalidateCacheNetworkWide(UUID uuid);

    /**
     * Saves the given player to the database.
     *
     * @param nucleoPlayer the player to save to the database
     */
    void savePlayerToDatabase(NucleoPlayer nucleoPlayer);

    /**
     * Returns the number of online players.
     *
     * @return the number of online players
     */
    default int onlinePlayerCount() {
        return onlinePlayers().size();
    }

    /**
     * Sends the given component to the player with the given UUID.
     *
     * @param receiver    the UUID of the player to send the component to
     * @param component   the component to send
     * @param messageType the type of the message
     */
    void send(
            UUID receiver,
            Component component,
            NucleoMessageSender.MessageType messageType
    );

    /**
     * Sends the given component to the player with the given UUID.
     *
     * @param nucleoOnlinePlayer the player to send the component to
     * @param component          the component to send
     * @param messageType        the type of the message
     */
    default void send(
            NucleoOnlinePlayer nucleoOnlinePlayer,
            Component component,
            NucleoMessageSender.MessageType messageType
    ) {
        send(nucleoOnlinePlayer.uuid(), component, messageType);
    }

    void executeCommand(
            NucleoOnlinePlayer nucleoOnlinePlayer,
            CommandTarget commandTarget,
            String command
    );

    void connectPlayer(
            NucleoOnlinePlayer nucleoOnlinePlayer,
            String server,
            Consumer<State> callback
    );

    default void connectPlayer(
            NucleoOnlinePlayer nucleoOnlinePlayer,
            String server
    ) {
        connectPlayer(nucleoOnlinePlayer, server, success -> {
        });
    }

    void kickPlayer(NucleoOnlinePlayer nucleoOnlinePlayer, Component reason, boolean raw);

    default void kickPlayer(NucleoOnlinePlayer nucleoOnlinePlayer, Component reason) {
        kickPlayer(nucleoOnlinePlayer, reason, false);
    }

    /**
     * Returns whether the player with the given UUID is online.
     *
     * @param uuid the UUID of the player
     * @return whether the player with the given UUID is online
     */
    default boolean isPlayerOnline(UUID uuid) {
        return onlinePlayer(uuid).isPresent();
    }

    default String playerName(UUID uuid) {
        return player(uuid).map(NucleoPlayer::name).orElse("unknown");
    }

}