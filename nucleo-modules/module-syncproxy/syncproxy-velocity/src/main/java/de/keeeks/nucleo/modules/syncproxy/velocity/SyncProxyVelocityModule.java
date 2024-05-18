package de.keeeks.nucleo.modules.syncproxy.velocity;

import de.keeeks.nucleo.core.api.Dependency;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.syncproxy.DefaultSyncProxyService;
import de.keeeks.nucleo.modules.syncproxy.velocity.commands.SyncProxyCommand;
import de.keeeks.nucleo.modules.syncproxy.velocity.listener.MaintenanceLoginListener;
import de.keeeks.nucleo.modules.syncproxy.velocity.listener.ProxyPingFixListener;
import de.keeeks.nucleo.modules.syncproxy.velocity.listener.ProxyPingListener;
import de.keeeks.nucleo.modules.syncproxy.velocity.listener.ProxyVersionPingListener;
import de.keeeks.nucleo.modules.syncproxy.velocity.packetlistener.ProxySyncProxyConfigurationUpdatePacketListener;
import de.keeeks.nucleo.syncproxy.api.SyncProxyConfiguration;
import de.keeeks.nucleo.syncproxy.api.SyncProxyService;

@ModuleDescription(
        name = "syncproxy",
        dependencies = {
                @Dependency(name = "config"),
                @Dependency(name = "database-mysql"),
                @Dependency(name = "messaging"),
                @Dependency(name = "players")

        }
)
public class SyncProxyVelocityModule extends VelocityModule {
    private SyncProxyService syncProxyService;

    @Override
    public void load() {
        syncProxyService = ServiceRegistry.registerService(
                SyncProxyService.class,
                new DefaultSyncProxyService(this)
        );
    }

    @Override
    public void enable() {
        registerListener(
                new ProxyPingListener(),
                new ProxyPingFixListener(),
                new ProxyVersionPingListener(),
                new MaintenanceLoginListener()
        );
        NatsConnection natsConnection = ServiceRegistry.service(NatsConnection.class);
        natsConnection.registerPacketListener(
                new ProxySyncProxyConfigurationUpdatePacketListener(proxyServer)
        );

        registerAutoCompletionSuggestions();

        registerCommands(
                new SyncProxyCommand()
        );
    }

    private void registerAutoCompletionSuggestions() {
        autoCompleter().registerSuggestion(
                "syncproxy:configurations",
                (list, commandActor, executableCommand) -> syncProxyService.configurations().stream().map(
                        SyncProxyConfiguration::name
                ).toList()
        );
    }
}