package de.keeeks.nucleo.modules.players.shared;

import de.keeeks.nucleo.modules.players.api.NucleoPlayer;

import java.time.Duration;
import java.time.Instant;

public record PlayerCacheElement<T extends NucleoPlayer>(
        T player,
        Instant lastAccess,
        boolean expire
) {

    public boolean expired(Duration duration) {
        return expire && lastAccess.plus(duration).isBefore(Instant.now());
    }

    public static <T extends NucleoPlayer> PlayerCacheElement<T> create(
            T player,
            boolean expire
    ) {
        return new PlayerCacheElement<>(player, Instant.now(), expire);
    }

    public static <T extends NucleoPlayer> PlayerCacheElement<T> create(
            T player
    ) {
        return create(player, true);
    }
}