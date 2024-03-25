package de.keeeks.nucleo.modules.hologram.spigot.holograms;

import de.keeeks.nucleo.modules.hologram.api.Hologram;
import de.keeeks.nucleo.modules.hologram.api.TextHologramLine;
import net.kyori.adventure.text.Component;

public class DefaultTextHologramLine extends DefaultHologramLine<Component> implements TextHologramLine {
    public DefaultTextHologramLine(Hologram hologram, Component content) {
        super(hologram, content);
    }

    @Override
    public double yOffSet() {
        return hologram.yOffset(this);
    }
}