package de.keeeks.nucleo.modules.moderation.tools.proxy;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.proxy.module.ProxyModule;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.proxy.commands.ClicksPerSecondCommand;
import de.keeeks.nucleo.modules.moderation.tools.proxy.listener.ModerationToolsPlayerDisconnectListener;
import de.keeeks.nucleo.modules.moderation.tools.shared.NucleoClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.shared.translation.ModerationToolsTranslationRegistry;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;

@ModuleDescription(
        name = "moderation-tools",
        description = "A module for moderation tools like e.g. click checks",
        depends = {"players", "messaging"}
)
public class ModerationToolsProxyModule extends ProxyModule {
    @Override
    public void load() {
        ServiceRegistry.registerService(
                ClickCheckApi.class,
                new NucleoClickCheckApi(ServiceRegistry.service(NatsConnection.class))
        );
        TranslationRegistry.initializeRegistry(new ModerationToolsTranslationRegistry(this));
    }

    @Override
    public void enable() {
        registerCommands(new ClicksPerSecondCommand());
        registerListener(new ModerationToolsPlayerDisconnectListener());
    }
}