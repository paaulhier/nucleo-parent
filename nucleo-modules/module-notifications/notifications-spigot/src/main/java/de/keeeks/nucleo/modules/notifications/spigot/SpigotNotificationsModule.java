package de.keeeks.nucleo.modules.notifications.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import de.keeeks.nucleo.modules.notifications.shared.NucleoNotificationApi;
import de.keeeks.nucleo.modules.notifications.shared.translation.NotificationsTranslationRegistry;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;

@ModuleDescription(
        name = "notifications",
        depends = {"config", "messaging", "database-mysql"},
        description = "Provides notifications for various events"
)
public class SpigotNotificationsModule extends SpigotModule {

    @Override
    public void load() {
        ServiceRegistry.registerService(
                NotificationApi.class,
                new NucleoNotificationApi(this)
        );
        TranslationRegistry.initializeRegistry(new NotificationsTranslationRegistry(this));
    }

}