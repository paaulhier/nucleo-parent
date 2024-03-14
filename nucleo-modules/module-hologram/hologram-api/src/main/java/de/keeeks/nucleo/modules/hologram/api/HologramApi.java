package de.keeeks.nucleo.modules.hologram.api;

import org.bukkit.Location;

import java.util.List;

public interface HologramApi {

    Hologram createHologram(
            String name,
            Location location
    );

    List<Hologram> holograms();

    Hologram hologram(String name);

    void removeHologram(String name);

    void removeHologram(Hologram hologram);

    void removeAllHolograms();

    void updateHologram(String name);

}