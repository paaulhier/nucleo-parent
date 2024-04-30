package de.keeeks.nucleo.modules.moderation.tools.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.shared.cps.NucleoClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.spigot.cps.ClicksPerSecondProvider;
import de.keeeks.nucleo.modules.moderation.tools.spigot.cps.clickcheck.ClickCheckMessage;
import de.keeeks.nucleo.modules.moderation.tools.spigot.listener.ModerationToolsClickListener;

@ModuleDescription(
        name = "moderation-tools",
        description = "A module for moderation tools like e.g. click checks",
        depends = {"players", "messaging", "lejet"}
)
public class ModerationToolsSpigotModule extends SpigotModule {

    @Override
    public void load() {
        ServiceRegistry.registerService(
                ClickCheckApi.class,
                new NucleoClickCheckApi(ServiceRegistry.service(NatsConnection.class))
        );
        ServiceRegistry.registerService(
                ClicksPerSecondProvider.class,
                new ClicksPerSecondProvider()
        );
    }

    @Override
    public void enable() {
        ServiceRegistry.registerService(
                ClickCheckMessage.class,
                new ClickCheckMessage(logger)
        );
        registerListener(new ModerationToolsClickListener());
    }
}