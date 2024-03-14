package de.keeeks.nucleo.modules.hologram.spigot;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.hologram.api.HologramApi;
import de.keeeks.nucleo.modules.hologram.spigot.config.ConfigurationHologramLoader;
import de.keeeks.nucleo.modules.hologram.spigot.holograms.DefaultHologramApi;
import de.keeeks.nucleo.modules.hologram.spigot.listener.HologramItemDespawnListener;

@ModuleDescription(
        name = "holograms",
        softDepends = "config"
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

        if (Module.module("config") != null) {
            ConfigurationHologramLoader.loadFromConfiguration();
        }
    }

    @Override
    public void disable() {
        ServiceRegistry.service(
                HologramApi.class
        ).removeAllHolograms();
    }
}