package de.keeeks.nucleo.modules.hologram.spigot.holograms;

import de.keeeks.nucleo.modules.hologram.api.Hologram;
import de.keeeks.nucleo.modules.hologram.api.HologramLine;
import net.kyori.adventure.text.Component;

public class DefaultTextHologramLine extends DefaultHologramLine<Component> implements HologramLine<Component> {
    public DefaultTextHologramLine(Hologram hologram, Component content) {
        super(hologram, content);
    }
}