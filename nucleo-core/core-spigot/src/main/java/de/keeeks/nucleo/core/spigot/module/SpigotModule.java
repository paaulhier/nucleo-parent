package de.keeeks.nucleo.core.spigot.module;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.spigot.NucleoSpigotPlugin;
import org.bukkit.event.Listener;

public abstract class SpigotModule extends Module {
    private final NucleoSpigotPlugin plugin = NucleoSpigotPlugin.plugin();

    public void registerCommands(Object... objects) {
        plugin.registerCommands(objects);
    }

    public void registerListener(Listener... listeners) {
        plugin.registerListener(listeners);
    }
}