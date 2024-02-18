package de.keeeks.nucleo.modules.players.api;

public interface NucleoOnlinePlayer extends NucleoPlayer {

    String server();

    String proxy();

    String ipAddress();

    Version version();

    NucleoOnlinePlayer updateServer(String server);

    NucleoOnlinePlayer updateProxy(String proxy);

    void update();

}