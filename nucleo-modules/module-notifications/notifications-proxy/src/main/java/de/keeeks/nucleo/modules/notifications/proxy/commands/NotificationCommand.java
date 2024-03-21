package de.keeeks.nucleo.modules.notifications.proxy.commands;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bungee.annotation.CommandPermission;

import java.util.List;
import java.util.UUID;

@Command({"notify", "notification", "notifications"})
@CommandPermission("nucleo.commands.notifications")
public final class NotificationCommand {
    private final NotificationApi notificationApi = ServiceRegistry.service(NotificationApi.class);

    @DefaultFor({"notify", "notification", "notifications"})
    public void notificationCommand(Audience audience) {
        sendHelpMessage(audience);
    }

    @Subcommand("help")
    public void helpCommand(Audience audience) {
        sendHelpMessage(audience);
    }

    @Subcommand("list")
    public void listCommand(Audience audience) {
        List<Notification> notifications = notificationApi.notifications();

        if (notifications.isEmpty()) {
            audience.sendMessage(Component.translatable("nucleo.notifications.command.list.empty"));
            return;
        }

        audience.sendMessage(Component.translatable("nucleo.notifications.command.list.header"));
        for (Notification notification : notifications) {
            audience.sendMessage(Component.translatable(
                    "nucleo.notifications.command.list.entry",
                    Component.text(notification.id()),
                    Component.text(notification.name()),
                    Component.text(notification.description())
            ));
        }
    }

    @AutoComplete("@notifications")
    @Usage("notifications toggle <Name|all>")
    @Subcommand("toggle")
    public void toggleCommand(Audience audience, String name) {
        notificationApi.notification(name).ifPresentOrElse(
                notification -> toggleNotification(audience, notification),
                () -> audience.sendMessage(Component.translatable(
                        "nucleo.notifications.command.toggle.notFound",
                        Component.text(name)
                ))
        );
    }

    @Usage("notifications enable <Name|all>")
    @AutoComplete("@notifications")
    @Subcommand("enable")
    public void enableCommand(Audience audience, String name) {
        if (name.equalsIgnoreCase("all")) {
            List<Notification> notifications = notificationApi.notifications();
            for (Notification notification : notifications) {
                enableNotification(audience, notification);
            }
            return;
        }

        notificationApi.notification(name).ifPresentOrElse(
                notification -> enableNotification(audience, notification),
                () -> audience.sendMessage(Component.translatable(
                        "nucleo.notifications.command.toggle.notFound",
                        Component.text(name)
                ))
        );
    }

    @Usage("notifications disable <Name|all>")
    @AutoComplete("@notifications")
    @Subcommand("disable")
    public void disableCommand(Audience audience, String name) {
        if (name.equalsIgnoreCase("all")) {
            List<Notification> notifications = notificationApi.notifications();
            for (Notification notification : notifications) {
                disableNotification(audience, notification);
            }
            return;
        }
        notificationApi.notification(name).ifPresentOrElse(
                notification -> disableNotification(audience, notification),
                () -> audience.sendMessage(Component.translatable(
                        "nucleo.notifications.command.toggle.notFound",
                        Component.text(name)
                ))
        );
    }

    private void enableNotification(Audience audience, Notification notification) {
        UUID uuid = audience.get(Identity.UUID).orElseThrow();
        if (notificationApi.notificationActive(notification, uuid)) {
            audience.sendMessage(Component.translatable(
                    "nucleo.notifications.command.enable.alreadyEnabled",
                    Component.text(notification.name())
            ));
            return;
        }
        notificationApi.notificationActive(notification, uuid, true);
        audience.sendMessage(Component.translatable(
                "nucleo.notifications.command.enable",
                Component.text(notification.name())
        ));
    }

    private void disableNotification(Audience audience, Notification notification) {
        UUID uuid = audience.get(Identity.UUID).orElseThrow();
        if (!notificationApi.notificationActive(notification, uuid)) {
            audience.sendMessage(Component.translatable(
                    "nucleo.notifications.command.disable.alreadyDisabled",
                    Component.text(notification.name())
            ));
            return;
        }
        notificationApi.notificationActive(notification, uuid, false);
        audience.sendMessage(Component.translatable(
                "nucleo.notifications.command.disable",
                Component.text(notification.name())
        ));
    }

    private void toggleNotification(Audience audience, Notification notification) {
        UUID uuid = audience.get(Identity.UUID).orElseThrow();
        boolean active = notificationApi.notificationActive(notification, uuid);
        if (active) {
            disableNotification(audience, notification);
        } else {
            enableNotification(audience, notification);
        }
    }

    private static void sendHelpMessage(Audience audience) {
        audience.sendMessage(Component.translatable("nucleo.notifications.command.usage"));
    }
}