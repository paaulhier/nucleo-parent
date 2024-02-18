package de.keeeks.nucleo.modules.players.shared;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.Skin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class DefaultNucleoPlayer implements NucleoPlayer {
    private static final PlayerService playerService = ServiceRegistry.service(
            PlayerService.class
    );

    private final UUID uuid;

    private String name;
    private Locale locale;
    private Skin skin;

    private long onlineTime;

    private Instant lastLogin;
    private Instant lastLogout;
    private Instant createdAt;
    private Instant updatedAt;

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
            Locale locale,
            Skin skin,
            long onlineTime,
            Instant lastLogin,
            Instant lastLogout,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.uuid = uuid;
        this.name = name;
        this.locale = locale;
        this.skin = skin;
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
    public NucleoPlayer updateLocale(Locale locale) {
        this.locale = locale;
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
    public NucleoPlayer updateOnlineTime(long onlineTime) {
        this.onlineTime = onlineTime;
        this.updatedAt = Instant.now();
        return this;
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
}