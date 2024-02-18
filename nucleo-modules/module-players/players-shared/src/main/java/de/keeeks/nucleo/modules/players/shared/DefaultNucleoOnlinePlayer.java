package de.keeeks.nucleo.modules.players.shared;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.Version;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public class DefaultNucleoOnlinePlayer extends DefaultNucleoPlayer implements NucleoOnlinePlayer {
    private static final PlayerService playerService = ServiceRegistry.service(
            PlayerService.class
    );

    private final String ipAddress;
    private final Version version;

    private String proxy;
    private String server;

    public DefaultNucleoOnlinePlayer(UUID uuid, String ipAddress, Version version) {
        super(uuid);
        this.ipAddress = ipAddress;
        this.version = version;
    }

    public DefaultNucleoOnlinePlayer(
            NucleoPlayer nucleoPlayer,
            String proxy,
            String server,
            String ipAddress,
            Version version
    ) {
        super(
                nucleoPlayer.uuid(),
                nucleoPlayer.name(),
                nucleoPlayer.locale(),
                nucleoPlayer.skin(),
                nucleoPlayer.onlineTime(),
                nucleoPlayer.lastLogin(),
                nucleoPlayer.lastLogout(),
                nucleoPlayer.createdAt(),
                nucleoPlayer.updatedAt()
        );
        this.proxy = proxy;
        this.server = server;
        this.ipAddress = ipAddress;
        this.version = version;

        properties().setProperties(nucleoPlayer.properties());
    }

    public DefaultNucleoOnlinePlayer(UUID uuid, String name, String ipAddress, Version version) {
        super(uuid, name);
        this.ipAddress = ipAddress;
        this.version = version;
    }

    @Override
    public NucleoPlayer addOnlineTime(long onlineTime) {
        return super.addOnlineTime(onlineTime);
    }

    @Override
    public NucleoOnlinePlayer updateServer(String server) {
        this.server = server;
        return this;
    }

    @Override
    public NucleoOnlinePlayer updateProxy(String proxy) {
        this.proxy = proxy;
        return this;
    }

    @Override
    public void update() {
        Module.module("players").logger().info("Network wide update for " + name() + " (UUID: " + uuid() + ")");
        playerService.updateNetworkWide(this);
    }
}
