package de.keeeks.nucleo.modules.syncproxy.proxy;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.proxy.module.ProxyModule;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.syncproxy.DefaultSyncProxyService;
import de.keeeks.nucleo.modules.syncproxy.proxy.commands.SyncProxyCommand;
import de.keeeks.nucleo.modules.syncproxy.proxy.configuration.SyncProxyKickScreenConfiguration;
import de.keeeks.nucleo.modules.syncproxy.proxy.listener.MaintenanceLoginListener;
import de.keeeks.nucleo.modules.syncproxy.proxy.listener.ProxyPingListener;
import de.keeeks.nucleo.modules.syncproxy.proxy.listener.ProxyVersionPingListener;
import de.keeeks.nucleo.modules.syncproxy.proxy.packetlistener.ProxySyncProxyConfigurationUpdatePacketListener;
import de.keeeks.nucleo.modules.syncproxy.proxy.translation.SyncProxyTranslationRegistry;
import de.keeeks.nucleo.syncproxy.api.SyncProxyConfiguration;
import de.keeeks.nucleo.syncproxy.api.SyncProxyService;

@ModuleDescription(
        name = "syncproxy",
        depends = {"config", "database-mysql", "messaging", "players"}
)
public class SyncProxyProxyModule extends ProxyModule {
    private SyncProxyService syncProxyService;

    @Override
    public void load() {
        syncProxyService = ServiceRegistry.registerService(
                SyncProxyService.class,
                new DefaultSyncProxyService(this)
        );
        new SyncProxyTranslationRegistry(this);
    }

    @Override
    public void enable() {
        SyncProxyKickScreenConfiguration syncProxyKickScreenConfiguration = JsonConfiguration.create(
                dataFolder(),
                "kick-screen"
        ).loadObject(
                SyncProxyKickScreenConfiguration.class,
                SyncProxyKickScreenConfiguration.defaultConfiguration()
        );
        registerListener(
                new ProxyPingListener(),
                new ProxyVersionPingListener(),
                new MaintenanceLoginListener(syncProxyKickScreenConfiguration)
        );
        NatsConnection natsConnection = ServiceRegistry.service(NatsConnection.class);
        natsConnection.registerPacketListener(
                new ProxySyncProxyConfigurationUpdatePacketListener(syncProxyKickScreenConfiguration)
        );

        registerAutoCompletionSuggestions();

        registerCommands(
                new SyncProxyCommand(audiences())
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