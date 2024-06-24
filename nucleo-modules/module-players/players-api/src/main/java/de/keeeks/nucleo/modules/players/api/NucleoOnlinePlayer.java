package de.keeeks.nucleo.modules.players.api;

import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerConnectResponsePacket.State;
import net.kyori.adventure.text.Component;

import java.util.function.Consumer;

public interface NucleoOnlinePlayer extends NucleoPlayer, NucleoMessageSender {

    /**
     * Returns the server the player is currently connected to.
     *
     * @return the server the player is currently connected to
     */
    String server();

    /**
     * Returns the proxy the player is currently connected to.
     *
     * @return the proxy the player is currently connected to
     */
    String proxy();

    /**
     * Returns the IP address of the player.
     *
     * @return the IP address of the player
     */
    String ipAddress();

    /**
     * Returns the version of the player. See {@link Version}.
     *
     * @return the version of the player
     */
    Version version();

    /**
     * Returns the online state of the player. See {@link OnlineState}.
     *
     * @return the online state of the player
     */
    OnlineState onlineState();

    /**
     * Returns the client brand of the player. See {@link ClientBrand}.
     *
     * @return the client brand of the player
     */
    ClientBrand clientBrand();

    /**
     * Updates the online state of the player. See {@link OnlineState}.
     *
     * @param onlineState the new online state
     * @return this player
     */
    NucleoOnlinePlayer updateOnlineState(OnlineState onlineState);

    /**
     * Updates the server the player is currently connected to.
     *
     * @param server the new server
     * @return this player
     */
    NucleoOnlinePlayer updateServer(String server);

    @Deprecated
    NucleoOnlinePlayer updateProxy(String proxy);

    /**
     * Updates the client brand of the player. See {@link ClientBrand}.
     *
     * @param clientBrand the new client brand
     * @return this player
     */
    NucleoOnlinePlayer updateClientBrand(ClientBrand clientBrand);

    /**
     * Connects the player to the specified server.
     *
     * @param server the server to connect to
     */
    default void connect(String server) {
        connect(server, success -> {
        });
    }

    /**
     * Connects the player to the specified server.
     *
     * @param server          the server to connect to
     * @param successCallback the callback that will be called when the player has been connected.
     *                        The callback will be called with the new state of the player.
     */
    void connect(String server, Consumer<State> successCallback);

    /**
     * Executes a command as the player. The command will be executed on the server the player is currently connected to.
     *
     * @param commandTarget the target of the command
     * @param command       the command to execute
     */
    void executeCommand(CommandTarget commandTarget, String command);

    /**
     * Kicks the player from the network.
     *
     * @param reason the reason for the kick
     * @param raw    whether the reason should be displayed as raw text
     */
    void kick(Component reason, boolean raw);

    /**
     * Kicks the player from the network.
     *
     * @param reason the reason for the kick
     */
    default void kick(Component reason) {
        kick(reason, false);
    }

    /**
     * Updates the player.
     */
    void update();

}