package de.keeeks.nucleo.modules.hologram.spigot.holograms;

import de.keeeks.nucleo.modules.hologram.api.Hologram;
import de.keeeks.nucleo.modules.hologram.api.ItemHologramLine;
import org.bukkit.inventory.ItemStack;

public class DefaultItemHologramLine extends DefaultHologramLine<ItemStack> implements ItemHologramLine {
    protected DefaultItemHologramLine(Hologram hologram, ItemStack content) {
        super(hologram, content);
    }

    @Override
    public double yOffSet() {
        return hologram.yOffset(this);
    }
}