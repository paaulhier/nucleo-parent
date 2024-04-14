package de.keeeks.nucleo.modules.syncproxy.velocity;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.syncproxy.DefaultSyncProxyService;
import de.keeeks.nucleo.modules.syncproxy.velocity.commands.SyncProxyCommand;
import de.keeeks.nucleo.modules.syncproxy.velocity.configuration.SyncProxyKickScreenConfiguration;
import de.keeeks.nucleo.modules.syncproxy.velocity.listener.MaintenanceLoginListener;
import de.keeeks.nucleo.modules.syncproxy.velocity.listener.ProxyPingFixListener;
import de.keeeks.nucleo.modules.syncproxy.velocity.listener.ProxyPingListener;
import de.keeeks.nucleo.modules.syncproxy.velocity.listener.ProxyVersionPingListener;
import de.keeeks.nucleo.modules.syncproxy.velocity.packetlistener.ProxySyncProxyConfigurationUpdatePacketListener;
import de.keeeks.nucleo.modules.syncproxy.velocity.translation.SyncProxyTranslationRegistry;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;
import de.keeeks.nucleo.syncproxy.api.SyncProxyConfiguration;
import de.keeeks.nucleo.syncproxy.api.SyncProxyService;

@ModuleDescription(
        name = "syncproxy",
        depends = {"config", "database-mysql", "messaging", "players"}
)
public class SyncProxyVelocityModule extends VelocityModule {
    private SyncProxyService syncProxyService;

    @Override
    public void load() {
        syncProxyService = ServiceRegistry.registerService(
                SyncProxyService.class,
                new DefaultSyncProxyService(this)
        );
        TranslationRegistry.initializeRegistry(new SyncProxyTranslationRegistry(this));
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
                new ProxyPingFixListener(),
                new ProxyVersionPingListener(),
                new MaintenanceLoginListener(syncProxyKickScreenConfiguration)
        );
        NatsConnection natsConnection = ServiceRegistry.service(NatsConnection.class);
        natsConnection.registerPacketListener(
                new ProxySyncProxyConfigurationUpdatePacketListener(syncProxyKickScreenConfiguration)
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