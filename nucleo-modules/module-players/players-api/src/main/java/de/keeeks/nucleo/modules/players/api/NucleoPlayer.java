package de.keeeks.nucleo.modules.players.api;

import java.time.Instant;
import java.util.UUID;

public interface NucleoPlayer {

    UUID uuid();

    String name();

    NucleoPlayer updateName(String name);

    Skin skin();

    NucleoPlayer updateSkin(String value, String signature);

    String lastIpAddress();

    NucleoPlayer updateLastIpAddress(String lastIpAddress);

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