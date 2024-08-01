package de.keeeks.nucleo.modules.players.shared;

import de.keeeks.nucleo.modules.players.api.*;
import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerConnectResponsePacket.State;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class DefaultNucleoOnlinePlayer extends DefaultNucleoPlayer implements NucleoOnlinePlayer {
    @Getter
    private final String ipAddress;
    @Getter
    private final Version version;

    @Getter
    @NonNull
    private OnlineState onlineState = OnlineState.ONLINE;
    @NonNull
    private ClientBrand clientBrand = ClientBrand.VANILLA;

    @Getter
    private String proxy;
    @Getter
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
            @NonNull OnlineState onlineState
    ) {
        super(
                nucleoPlayer.uuid(),
                nucleoPlayer.name(),
                nucleoPlayer.skin(),
                nucleoPlayer.lastIpAddress(),
                nucleoPlayer.onlineTime(),
                List.of(),
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
    public String lastIpAddress() {
        return this.ipAddress;
    }

    @Override
    public Optional<ClientBrand> clientBrand() {
        return Optional.of(clientBrand);
    }

    @Override
    public NucleoOnlinePlayer updateOnlineState(@NotNull OnlineState onlineState) {
        Validate.notNull(onlineState, "The online state cannot be null");
        this.onlineState = onlineState;
        return this;
    }

    @Override
    public NucleoOnlinePlayer updateServer(String server) {
        this.server = server;
        return this;
    }

    @Override
    public NucleoOnlinePlayer updateClientBrand(@NotNull ClientBrand clientBrand) {
        Validate.notNull(clientBrand, "The client brand cannot be null");
        this.clientBrand = clientBrand;
        return this;
    }

    @Override
    public void connect(String server, Consumer<State> successCallback) {
        playerService().connectPlayer(
                this,
                server,
                successCallback
        );
    }

    @Override
    public void executeCommand(CommandTarget commandTarget, String command) {
        playerService().executeCommand(
                this,
                commandTarget,
                command
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
