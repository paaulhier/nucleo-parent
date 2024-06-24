package de.keeeks.nucleo.modules.vanish.api;

import java.util.UUID;

public interface VanishApi {
    String CHANNEL = "nucleo:vanish";

    /**
     * Get the vanish data of a player
     *
     * @param uuid The UUID of the player
     * @return The vanish data
     */
    VanishData vanishData(UUID uuid);

    /**
     * Invalidates the vanish data of a player
     *
     * @param uuid The UUID of the player
     */
    void invalidate(UUID uuid);
}