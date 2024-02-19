package de.keeeks.nucleo.modules.syncproxy.proxy;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.proxy.NucleoProxyPlugin;
import de.keeeks.nucleo.core.proxy.module.ProxyModule;
import de.keeeks.nucleo.modules.syncproxy.DefaultSyncProxyService;
import de.keeeks.nucleo.modules.syncproxy.proxy.listener.ProxyPingListener;
import de.keeeks.nucleo.modules.syncproxy.proxy.listener.ProxyVersionPingListener;
import de.keeeks.nucleo.syncproxy.api.configuration.SyncProxyService;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ProxyServer;

@ModuleDescription(
        name = "syncproxy",
        depends = {"config", "database-mysql", "messaging", "players"}
)
public class SyncProxyProxyModule extends ProxyModule {
    private final BungeeAudiences bungeeAudiences = BungeeAudiences.create(
            NucleoProxyPlugin.plugin()
    );

    @Override
    public void load() {
        ServiceRegistry.registerService(
                SyncProxyService.class,
                new DefaultSyncProxyService(this)
        );
    }

    @Override
    public void enable() {
        registerListener(
                new ProxyPingListener(this),
                new ProxyVersionPingListener()
        );

        bungeeAudiences.console().sendMessage(MiniMessage.miniMessage().deserialize(
                "<dark_gray>● <gradient:570003:#fa0008>Development</gradient> <dark_gray>●"
        ));
    }
}