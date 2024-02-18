package de.keeeks.nucleo.modules.players.api;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

public interface NucleoPlayer {

    UUID uuid();

    String name();

    NucleoPlayer updateName(String name);

    Locale locale();

    NucleoPlayer updateLocale(Locale locale);

    Skin skin();

    NucleoPlayer updateSkin(String value, String signature);

    default NucleoPlayer updateSkin(String value) {
        return updateSkin(
                value,
                null
        );
    }

    long onlineTime();

    NucleoPlayer updateOnlineTime(long onlineTime);

    default NucleoPlayer addOnlineTime(long onlineTime) {
        return updateOnlineTime(
                onlineTime() + onlineTime
        );
    }

    PropertyHolder properties();

    NucleoPlayer updateLastLogin();

    NucleoPlayer updateLastLogout();

    void update();

    Instant createdAt();

    Instant lastLogin();

    Instant lastLogout();

    Instant updatedAt();

}