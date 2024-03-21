package de.keeeks.nucleo.modules.notifications.proxy;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.proxy.module.ProxyModule;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import de.keeeks.nucleo.modules.notifications.proxy.commands.NotificationCommand;
import de.keeeks.nucleo.modules.notifications.shared.NucleoNotificationApi;
import de.keeeks.nucleo.modules.notifications.shared.translation.NotificationsTranslationRegistry;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;
import org.apache.commons.collections4.ListUtils;

import java.util.List;

@ModuleDescription(
        name ="notifications",
        depends = {"config", "messaging", "database-mysql"},
        description = "Provides notifications for various events"
)
public class NotificationsProxyModule extends ProxyModule {
    private NotificationApi notificationApi;

    @Override
    public void load() {
        notificationApi = ServiceRegistry.registerService(
                NotificationApi.class,
                new NucleoNotificationApi(this)
        );
        TranslationRegistry.initializeRegistry(new NotificationsTranslationRegistry(this));
    }

    @Override
    public void enable() {
        autoCompleter().registerSuggestion(
                "notifications",
                (list, commandActor, executableCommand) -> notificationApi.notifications().stream().map(
                        Notification::name
                ).toList()
        );

        registerCommands(new NotificationCommand());
    }
}