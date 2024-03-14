package de.keeeks.nucleo.modules.hologram.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.hologram.api.Hologram;
import de.keeeks.nucleo.modules.hologram.api.HologramApi;
import de.keeeks.nucleo.modules.hologram.spigot.holograms.DefaultHologramApi;
import de.keeeks.nucleo.modules.hologram.spigot.listener.HologramItemDespawnListener;
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
        registerListener(new HologramItemDespawnListener());
    }

    @Override
    public void disable() {
        ServiceRegistry.service(
                HologramApi.class
        ).removeAllHolograms();
    }
}