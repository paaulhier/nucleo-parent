package de.keeeks.nucleo.core.proxy.module;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.proxy.NucleoProxyPlugin;
import lombok.Getter;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.PluginManager;
import revxrsal.commands.autocomplete.AutoCompleter;

@Getter
public abstract class ProxyModule extends Module {
    private final NucleoProxyPlugin plugin = NucleoProxyPlugin.plugin();

    public BungeeAudiences audiences() {
        return plugin.bungeeAudiences();
    }

    public void registerCommands(Object... commands) {
        plugin.registerCommands(commands);
    }

    public AutoCompleter autoCompleter() {
        return plugin.autoCompleter();
    }

    public void registerListener(Listener... listeners) {
        PluginManager pluginManager = plugin.getProxy().getPluginManager();
        for (Listener listener : listeners) {
            pluginManager.registerListener(NucleoProxyPlugin.plugin(), listener);
        }
    }
}