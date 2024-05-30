package de.keeeks.nucleo.core.velocity.module;

import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.core.command.CommandSupportingModule;
import de.keeeks.nucleo.core.velocity.NucleoVelocityPlugin;
import lombok.Getter;
import revxrsal.commands.velocity.VelocityCommandHandler;

@Getter
public abstract class VelocityModule extends CommandSupportingModule {
    protected static final NucleoVelocityPlugin plugin = NucleoVelocityPlugin.plugin();
    protected final ProxyServer proxyServer = plugin.proxyServer();

    public VelocityModule() {
        super(plugin.commandHandler());
    }

    public void registerListener(Object... listeners) {
        EventManager eventManager = plugin.proxyServer().getEventManager();
        for (Object listener : listeners) {
            eventManager.register(plugin, listener);
        }
    }

    public NucleoVelocityPlugin plugin() {
        return plugin;
    }
}