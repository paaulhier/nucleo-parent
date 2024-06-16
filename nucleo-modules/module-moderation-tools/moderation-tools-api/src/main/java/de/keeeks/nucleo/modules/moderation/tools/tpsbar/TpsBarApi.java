package de.keeeks.nucleo.modules.moderation.tools.tpsbar;

import java.util.UUID;

public interface TpsBarApi {
    String CHANNEL = "tpsbar";

    /**
     * Checks whether the TPS bar is enabled for the specified player.
     * @param uuid The player's UUID
     * @return Whether the TPS bar is enabled
     */
    boolean enabled(UUID uuid);

    /**
     * Enables or disables the TPS bar for the specified player.
     * @param uuid The player's UUID
     * @param enabled Whether the TPS bar should be enabled
     */
    void enabled(UUID uuid, boolean enabled);

    /**
     * Toggles the TPS bar for the specified player.
     * @param uuid The player's UUID
     * @return The new state of the TPS bar
     */
    default boolean toggle(UUID uuid) {
        boolean enabled = !enabled(uuid);
        enabled(uuid, enabled);
        return enabled;
    }
}