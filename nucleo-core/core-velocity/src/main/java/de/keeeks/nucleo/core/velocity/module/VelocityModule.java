package de.keeeks.nucleo.core.velocity.module;

import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.velocity.NucleoVelocityPlugin;
import lombok.Getter;
import revxrsal.commands.autocomplete.AutoCompleter;

import java.util.function.Consumer;

@Getter
public abstract class VelocityModule extends Module {
    protected final NucleoVelocityPlugin plugin = NucleoVelocityPlugin.plugin();
    protected final ProxyServer proxyServer = plugin.proxyServer();

    public void registerCommands(Object... commands) {
        plugin.registerCommands(commands);
    }

    public AutoCompleter autoCompleter() {
        return plugin.autoCompleter();
    }

    public void registerListener(Object... listeners) {
        EventManager eventManager = plugin.proxyServer().getEventManager();
        for (Object listener : listeners) {
            eventManager.register(plugin, listener);
        }
    }
}