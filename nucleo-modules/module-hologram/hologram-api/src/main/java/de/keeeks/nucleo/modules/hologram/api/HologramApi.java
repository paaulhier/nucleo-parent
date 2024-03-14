package de.keeeks.nucleo.modules.hologram.api;

import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public interface HologramApi {

    Hologram createHologram(
            String name,
            Location location
    );

    List<Hologram> holograms();

    Hologram hologram(String name);

    Hologram hologram(UUID uuid);

    void removeHologram(String name);

    void removeHologram(Hologram hologram);

    void removeAllHolograms();

    void updateHologram(String name);

    void removeHologramFromCache(Hologram hologram);
}