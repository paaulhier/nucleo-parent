package de.keeeks.nucleo.modules.tabdecoration;

import de.keeeks.nucleo.core.api.Dependency;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.tabdecoration.listener.TabDecorationLoginListener;
import de.keeeks.nucleo.modules.tabdecoration.packetlistener.*;
import de.keeeks.nucleo.modules.tabdecoration.service.TabDecorationService;

@ModuleDescription(
        name = "tabdecoration",
        dependencies = {
                @Dependency(name = "players"),
                @Dependency(name = "syncproxy"),
                @Dependency(name = "lejet")
        }
)
public class TabDecorationModule extends VelocityModule {
    @Override
    public void load() {
        ServiceRegistry.registerService(
                TabDecorationService.class,
                new TabDecorationService()
        );
    }

    @Override
    public void enable() {
        registerListener(new TabDecorationLoginListener());

        ServiceRegistry.service(NatsConnection.class).registerPacketListener(
                new TabDecorationNucleoPlayerUpdatePacketListener(proxyServer),
                new TabDecorationPermissionUserUpdatePacketListener(proxyServer),
                new TabDecorationNucleoPlayerInvalidatePacketListener(proxyServer),
                new TabDecorationNucleoOnlinePlayerUpdatePacketListener(proxyServer),
                new TabDecorationSyncProxyConfigurationUpdatePacketListener(proxyServer)
        );
    }
}