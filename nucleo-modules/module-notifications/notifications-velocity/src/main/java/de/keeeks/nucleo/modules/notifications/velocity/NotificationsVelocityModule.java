package de.keeeks.nucleo.modules.notifications.velocity;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import de.keeeks.nucleo.modules.notifications.shared.NucleoNotificationApi;
import de.keeeks.nucleo.modules.notifications.velocity.commands.NotificationCommand;
import de.keeeks.nucleo.modules.notifications.velocity.commands.NotificationStateType;
import revxrsal.commands.velocity.VelocityCommandActor;

@ModuleDescription(
        name = "notifications",
        depends = {"config", "messaging", "database-mysql"},
        description = "Provides notifications for various events"
)
public class NotificationsVelocityModule extends VelocityModule {
    private NotificationApi notificationApi;

    @Override
    public void load() {
        notificationApi = ServiceRegistry.registerService(
                NotificationApi.class,
                new NucleoNotificationApi(this)
        );
    }

    @Override
    public void enable() {
        autoCompleter().registerSuggestion(
                "notifications",
                (list, commandActor, executableCommand) -> notificationApi.notifications().stream().filter(
                        notification -> {
                            if (commandActor instanceof VelocityCommandActor velocityCommandActor) {
                                String requiredPermission = notification.requiredPermission();
                                if (requiredPermission == null) return true;
                                Player player = velocityCommandActor.getAsPlayer();
                                if (player == null) return false;
                                return player.hasPermission(requiredPermission);
                            }
                            return false;
                        }
                ).map(
                        Notification::name
                ).toList()
        );
        commandHandler().registerValueResolver(
                NotificationStateType.class,
                valueResolverContext -> {
                    String typeName = valueResolverContext.pop();
                    return NotificationStateType.byName(typeName);
                }
        );

        commandHandler().registerValueResolver(
                Notification.class,
                valueResolverContext -> {
                    String notificationName = valueResolverContext.pop();
                    return notificationApi.notification(notificationName).orElse(null);
                }
        );

        registerCommands(new NotificationCommand());
    }
}