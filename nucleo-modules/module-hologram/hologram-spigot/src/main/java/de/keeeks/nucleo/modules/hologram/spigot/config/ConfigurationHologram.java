package de.keeeks.nucleo.modules.hologram.spigot.config;

import org.bukkit.Location;

public record ConfigurationHologram(
        String name,
        Location location,
        String[] lines
) {
}