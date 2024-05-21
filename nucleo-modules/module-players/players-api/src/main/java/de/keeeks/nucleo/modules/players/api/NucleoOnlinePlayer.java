package de.keeeks.nucleo.modules.players.api;

import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerConnectResponsePacket.State;
import net.kyori.adventure.text.Component;

import java.util.function.Consumer;

public interface NucleoOnlinePlayer extends NucleoPlayer, NucleoMessageSender {

    String server();

    String proxy();

    String ipAddress();

    Version version();

    OnlineState onlineState();

    NucleoOnlinePlayer updateOnlineState(OnlineState onlineState);

    NucleoOnlinePlayer updateServer(String server);

    NucleoOnlinePlayer updateProxy(String proxy);

    default void connect(String server) {
        connect(server, success -> {
        });
    }

    void connect(String server, Consumer<State> successCallback);

    void executeCommand(CommandTarget commandTarget, String command);

    void kick(Component reason, boolean raw);

    default void kick(Component reason) {
        kick(reason, false);
    }

    void update();
}