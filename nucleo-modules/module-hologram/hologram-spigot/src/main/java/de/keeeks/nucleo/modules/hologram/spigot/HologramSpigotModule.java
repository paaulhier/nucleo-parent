package de.keeeks.nucleo.modules.hologram.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.hologram.api.Hologram;
import de.keeeks.nucleo.modules.hologram.api.HologramApi;
import de.keeeks.nucleo.modules.hologram.spigot.holograms.DefaultHologramApi;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@ModuleDescription(
        name = "holograms"
)
public class HologramSpigotModule extends SpigotModule {
    @Override
    public void load() {
        ServiceRegistry.registerService(
                HologramApi.class,
                new DefaultHologramApi()
        );
    }

    @Override
    public void enable() {
        Hologram hologram = ServiceRegistry.service(
                HologramApi.class
        ).createHologram(
                "test",
                new Location(Bukkit.getWorld("world"), 0, 100, 0)
        );
        hologram.addLine(Component.translatable("test"));
        hologram.addLine(new ItemStack(Material.DIAMOND));
        hologram.addLine(Component.text("Nicht übersetzter Text"));
        hologram.addLine(new ItemStack(Material.EMERALD_BLOCK));
        hologram.addLine(Component.text("Nicht übersetzter Text"));
        hologram.addLine(new ItemStack(Material.EMERALD_BLOCK));
        hologram.addLine(Component.text("Nicht übersetzter Text"));
        hologram.addLine(new ItemStack(Material.EMERALD_BLOCK));
    }

    @Override
    public void disable() {
        ServiceRegistry.service(
                HologramApi.class
        ).removeAllHolograms();
    }
}