package de.keeeks.nucleo.modules.players.shared;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.PropertyHolder;
import de.keeeks.nucleo.modules.players.api.Skin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class DefaultNucleoPlayer implements NucleoPlayer {
    private static final PlayerService playerService = ServiceRegistry.service(
            PlayerService.class
    );

    protected final PropertyHolder propertyHolder = new DefaultPropertyHolder();

    protected final UUID uuid;

    protected String name;
    protected Skin skin;
    protected String lastIpAddress;

    protected long onlineTime;

    protected Instant lastLogin;
    protected Instant lastLogout;
    protected Instant createdAt;
    protected Instant updatedAt;

    public DefaultNucleoPlayer(UUID uuid, String name) {
        Instant now = Instant.now();
        this.uuid = uuid;
        this.name = name;
        this.createdAt = now;
        this.updatedAt = now;
    }

    public DefaultNucleoPlayer(
            UUID uuid,
            String name,
            Skin skin,
            String lastIpAddress,
            long onlineTime,
            Instant lastLogin,
            Instant lastLogout,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.uuid = uuid;
        this.name = name;
        this.skin = skin;
        this.lastIpAddress = lastIpAddress;
        this.onlineTime = onlineTime;
        this.lastLogin = lastLogin;
        this.lastLogout = lastLogout;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public NucleoPlayer updateName(String name) {
        this.name = name;
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public NucleoPlayer updateSkin(String value, String signature) {
        this.skin = new Skin(uuid, value, signature);
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public NucleoPlayer updateLastIpAddress(String lastIpAddress) {
        this.lastIpAddress = lastIpAddress;
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public NucleoPlayer updateOnlineTime(long onlineTime) {
        this.onlineTime = onlineTime;
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public PropertyHolder properties() {
        return propertyHolder;
    }

    @Override
    public NucleoPlayer updateLastLogin() {
        this.lastLogin = Instant.now();
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public NucleoPlayer updateLastLogout() {
        this.lastLogout = Instant.now();
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public void update() {
        playerService.updateNetworkWide(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DefaultNucleoPlayer that = (DefaultNucleoPlayer) object;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}