package de.keeeks.nucleo.modules.hologram.spigot.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public record ConfigurationHologramList(
        List<ConfigurationHologram> holograms
) {

    public static ConfigurationHologramList defaultConfiguration() {
        return new ConfigurationHologramList(List.of(
                new ConfigurationHologram(
                        "example",
                        new Location(Bukkit.getWorld("world"), 0, 100 ,0),
                        new String[]{"line1", "line2", "line3"}
                )
        ));
    }
}