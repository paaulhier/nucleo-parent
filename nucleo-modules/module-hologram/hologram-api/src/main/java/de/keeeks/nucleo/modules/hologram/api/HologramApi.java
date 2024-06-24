package de.keeeks.nucleo.modules.hologram.api;

import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public interface HologramApi {

    /**
     * Creates a new hologram with the given name and location.
     *
     * @param name     the name of the new hologram
     * @param location the location of the new hologram
     * @return the new hologram
     */
    Hologram createHologram(String name, Location location);

    /**
     * Returns a list of all holograms.
     *
     * @return a list of all holograms
     */
    List<Hologram> holograms();

    /**
     * Returns the hologram with the given name.
     *
     * @param name the name of the hologram
     * @return the hologram with the given name
     */
    Hologram hologram(String name);

    /**
     * Returns the hologram with the given unique identifier.
     *
     * @param uuid the unique identifier of the hologram
     * @return the hologram with the given unique identifier
     */
    Hologram hologram(UUID uuid);

    /**
     * Removes the hologram with the given name.
     *
     * @param name the name of the hologram
     */
    void removeHologram(String name);

    /**
     * Removes the given hologram.
     *
     * @param hologram the hologram to remove
     */
    void removeHologram(Hologram hologram);

    /**
     * Removes all holograms.
     */
    void removeAllHolograms();

    /**
     * Updates the hologram with the given name.
     *
     * @param name the name of the hologram
     */
    void updateHologram(String name);

    /**
     * Updates the given hologram.
     *
     * @param hologram the hologram to update
     */
    void removeHologramFromCache(Hologram hologram);
}