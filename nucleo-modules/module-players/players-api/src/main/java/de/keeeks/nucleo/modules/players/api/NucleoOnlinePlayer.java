package de.keeeks.nucleo.modules.players.api;

import java.util.function.Consumer;

public interface NucleoOnlinePlayer extends NucleoPlayer, NucleoMessageSender {

    String server();

    String proxy();

    String ipAddress();

    Version version();

    NucleoOnlinePlayer updateServer(String server);

    NucleoOnlinePlayer updateProxy(String proxy);

    default void connect(String server) {
        connect(server, success -> {});
    }

    void connect(String server, Consumer<Boolean> successCallback);

    void update();
}