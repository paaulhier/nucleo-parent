package de.keeeks.nucleo.core.spigot.module;

import de.keeeks.nucleo.core.command.CommandSupportingModule;
import de.keeeks.nucleo.core.spigot.NucleoSpigotPlugin;
import lombok.Getter;
import org.bukkit.event.Listener;

import java.util.Optional;

@Getter
public abstract class SpigotModule extends CommandSupportingModule {
    protected static final NucleoSpigotPlugin plugin = NucleoSpigotPlugin.plugin();

    public SpigotModule() {
        super(plugin.bukkitCommandHandler());
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

    public NucleoSpigotPlugin plugin() {
        return plugin;
    }
}