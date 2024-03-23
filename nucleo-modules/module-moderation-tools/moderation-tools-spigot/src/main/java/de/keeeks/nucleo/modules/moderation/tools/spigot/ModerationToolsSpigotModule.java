package de.keeeks.nucleo.modules.moderation.tools.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.shared.NucleoClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.shared.translation.ModerationToolsTranslationRegistry;
import de.keeeks.nucleo.modules.moderation.tools.spigot.cps.ClicksPerSecondProvider;
import de.keeeks.nucleo.modules.moderation.tools.spigot.cps.clickcheck.ClickCheckActionBar;
import de.keeeks.nucleo.modules.moderation.tools.spigot.listener.ModerationToolsClickListener;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;

@ModuleDescription(
        name ="moderation-tools",
        description = "A module for moderation tools like e.g. click checks",
        depends = {"players", "messaging"}
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
        TranslationRegistry.initializeRegistry(new ModerationToolsTranslationRegistry(this));
    }

    @Override
    public void enable() {
        ServiceRegistry.registerService(
                ClickCheckActionBar.class,
                new ClickCheckActionBar(logger)
        );
        registerListener(new ModerationToolsClickListener());
    }
}