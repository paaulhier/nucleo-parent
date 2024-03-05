package de.keeeks.nucleo.modules.syncproxy.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.syncproxy.DefaultSyncProxyService;
import de.keeeks.nucleo.syncproxy.api.configuration.SyncProxyService;

@ModuleDescription(
        name = "syncproxy",
        depends = {"config", "database-mysql", "messaging", "players"}
)
public class SyncProxySpigotModule extends SpigotModule {
    @Override
    public void load() {
        ServiceRegistry.registerService(
                SyncProxyService.class,
                new DefaultSyncProxyService(this)
        );
    }
}