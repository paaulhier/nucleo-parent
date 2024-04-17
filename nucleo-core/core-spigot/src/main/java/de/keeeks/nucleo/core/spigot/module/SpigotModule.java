package de.keeeks.nucleo.core.spigot.module;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.spigot.NucleoSpigotPlugin;
import lombok.Getter;
import org.bukkit.event.Listener;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.util.Optional;

@Getter
public abstract class SpigotModule extends Module {
    private final NucleoSpigotPlugin plugin = NucleoSpigotPlugin.plugin();

    public void registerCommands(Object... objects) {
        plugin.registerCommands(objects);
    }

    public BukkitCommandHandler commandHandler() {
        return plugin.bukkitCommandHandler();
    }

    public void registerListener(Listener... listeners) {
        plugin.registerListener(listeners);
    }

    public Optional<String> serverName() {
        return plugin.serverName();
    }

    public Optional<String> templateName() {
        return plugin.templateName();
    }

    public Optional<String> templateOrServerName() {
        return plugin.templateOrServerName();
    }

}