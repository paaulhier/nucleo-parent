package de.keeeks.nucleo.modules.players.shared;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.*;
import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
public class DefaultNucleoOnlinePlayer extends DefaultNucleoPlayer implements NucleoOnlinePlayer {
    private static PlayerService playerService;

    private final String ipAddress;
    private final Version version;

    private OnlineState onlineState = OnlineState.ONLINE;

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
            Version version,
            OnlineState onlineState
    ) {
        super(
                nucleoPlayer.uuid(),
                nucleoPlayer.name(),
                nucleoPlayer.skin(),
                nucleoPlayer.lastIpAddress(),
                nucleoPlayer.onlineTime(),
                nucleoPlayer.lastLogin(),
                nucleoPlayer.lastLogout(),
                nucleoPlayer.createdAt(),
                nucleoPlayer.updatedAt()
        );
        this.lastIpAddress = ipAddress;
        this.proxy = proxy;
        this.server = server;
        this.ipAddress = ipAddress;
        this.version = version;
        this.onlineState = onlineState;
        properties().setProperties(nucleoPlayer.properties());
    }

    public DefaultNucleoOnlinePlayer(UUID uuid, String name, String ipAddress, Version version) {
        super(uuid, name);
        this.ipAddress = ipAddress;
        this.version = version;
    }

    @Override
    public long onlineTime() {
        return super.onlineTime() + (Duration.between(
                lastLogin(),
                Instant.now()
        ).toMillis());
    }

    @Override
    public NucleoOnlinePlayer updateOnlineState(OnlineState onlineState) {
        this.onlineState = onlineState;
        return this;
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
    public void connect(String server, Consumer<Boolean> successCallback) {
        playerService().connectPlayer(
                this,
                server,
                successCallback
        );
    }

    @Override
    public void kick(Component reason, boolean raw) {
        playerService().kickPlayer(
                this,
                reason,
                raw
        );
    }

    @Override
    public void update() {
        playerService().updateNetworkWide(this);
    }

    private PlayerService playerService() {
        if (playerService == null) {
            return playerService = ServiceRegistry.service(PlayerService.class);
        }
        return playerService;
    }

    @Override
    public void send(Component component, MessageType messageType) {
        playerService.send(
                this,
                component,
                messageType
        );
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }
}
