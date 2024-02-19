package de.keeeks.nucleo.modules.syncproxy.proxy;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.proxy.module.ProxyModule;
import de.keeeks.nucleo.modules.syncproxy.DefaultSyncProxyService;
import de.keeeks.nucleo.modules.syncproxy.proxy.listener.MaintenanceLoginListener;
import de.keeeks.nucleo.modules.syncproxy.proxy.listener.ProxyPingListener;
import de.keeeks.nucleo.modules.syncproxy.proxy.listener.ProxyVersionPingListener;
import de.keeeks.nucleo.syncproxy.api.configuration.SyncProxyService;

@ModuleDescription(
        name = "syncproxy",
        depends = {"config", "database-mysql", "messaging", "players"}
)
public class SyncProxyProxyModule extends ProxyModule {

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
                new ProxyPingListener(),
                new ProxyVersionPingListener(),
                new MaintenanceLoginListener()
        );
    }
}