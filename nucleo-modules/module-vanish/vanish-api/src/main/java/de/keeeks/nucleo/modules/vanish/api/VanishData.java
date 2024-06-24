package de.keeeks.nucleo.modules.vanish.api;

import java.util.UUID;

public interface VanishData {
    /**
     * Get the UUID of the player
     *
     * @return The UUID
     */
    UUID uuid();

    /**
     * Check if the player is vanished
     *
     * @return True, if the player is vanished
     */
    boolean vanished();

    /**
     * Set the vanished state of the player
     *
     * @param vanished The new vanished state
     * @return True if the vanished state was changed
     */
    boolean vanished(boolean vanished);
}