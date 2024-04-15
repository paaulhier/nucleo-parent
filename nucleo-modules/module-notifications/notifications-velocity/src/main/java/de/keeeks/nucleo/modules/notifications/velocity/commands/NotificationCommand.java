package de.keeeks.nucleo.modules.notifications.velocity.commands;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.*;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.util.List;
import java.util.UUID;

@Command({"notify", "notification", "notifications"})
@CommandPermission("nucleo.commands.notifications")
public final class
NotificationCommand {
    private final NotificationApi notificationApi = ServiceRegistry.service(NotificationApi.class);

    @DefaultFor({"notify", "notification", "notifications"})
    public void notificationCommand(Player player) {
        sendHelpMessage(player);
    }

    @Subcommand("help")
    public void helpCommand(Player player) {
        sendHelpMessage(player);
    }

    @Subcommand("list")
    public void listCommand(
            Player player,
            @Optional @Default("all") NotificationStateType notificationStateType
    ) {
        List<Notification> notifications = notificationApi.notifications();

        if (notificationStateType == NotificationStateType.DISABLED) {
            notifications = List.copyOf(notifications).stream().filter(
                    notification -> !notificationApi.notificationActive(notification, player.getUniqueId())
            ).toList();
        } else if (notificationStateType == NotificationStateType.ENABLED) {
            notifications = List.copyOf(notifications).stream().filter(
                    notification -> notificationApi.notificationActive(notification, player.getUniqueId())
            ).toList();
        }

        if (notifications.isEmpty()) {
            player.sendMessage(Component.translatable("nucleo.notifications.command.list.empty"));
            return;
        }

        player.sendMessage(Component.translatable("nucleo.notifications.command.list.header"));
        for (Notification notification : notifications) {
            player.sendMessage(Component.translatable(
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
    public void toggleCommand(Player player, String name) {
        notificationApi.notification(name).ifPresentOrElse(
                notification -> toggleNotification(player, notification),
                () -> player.sendMessage(Component.translatable(
                        "nucleo.notifications.command.toggle.notFound",
                        Component.text(name)
                ))
        );
    }

    @Usage("notifications enable <Name|all>")
    @AutoComplete("@notifications")
    @Subcommand("enable")
    public void enableCommand(Player player, String name) {
        if (name.equalsIgnoreCase("all")) {
            List<Notification> notifications = notificationApi.notifications();
            for (Notification notification : notifications) {
                enableNotification(player, notification);
            }
            return;
        }

        notificationApi.notification(name).ifPresentOrElse(
                notification -> enableNotification(player, notification),
                () -> player.sendMessage(Component.translatable(
                        "nucleo.notifications.command.toggle.notFound",
                        Component.text(name)
                ))
        );
    }

    @Usage("notifications disable <Name|all>")
    @AutoComplete("@notifications")
    @Subcommand("disable")
    public void disableCommand(Player player, String name) {
        if (name.equalsIgnoreCase("all")) {
            List<Notification> notifications = notificationApi.notifications();
            for (Notification notification : notifications) {
                disableNotification(player, notification);
            }
            return;
        }
        notificationApi.notification(name).ifPresentOrElse(
                notification -> disableNotification(player, notification),
                () -> player.sendMessage(Component.translatable(
                        "nucleo.notifications.command.toggle.notFound",
                        Component.text(name)
                ))
        );
    }

    private void enableNotification(Player player, Notification notification) {
        UUID uuid = player.getUniqueId();
        if (notificationApi.notificationActive(notification, uuid)) {
            player.sendMessage(Component.translatable(
                    "nucleo.notifications.command.enable.alreadyEnabled",
                    Component.text(notification.name())
            ));
            return;
        }
        notificationApi.notificationActive(notification, uuid, true);
        player.sendMessage(Component.translatable(
                "nucleo.notifications.command.enable",
                Component.text(notification.name())
        ));
    }

    private void disableNotification(Player player, Notification notification) {
        UUID uuid = player.getUniqueId();
        if (!notificationApi.notificationActive(notification, uuid)) {
            player.sendMessage(Component.translatable(
                    "nucleo.notifications.command.disable.alreadyDisabled",
                    Component.text(notification.name())
            ));
            return;
        }
        notificationApi.notificationActive(notification, uuid, false);
        player.sendMessage(Component.translatable(
                "nucleo.notifications.command.disable",
                Component.text(notification.name())
        ));
    }

    private void toggleNotification(Player player, Notification notification) {
        UUID uuid = player.getUniqueId();
        boolean active = notificationApi.notificationActive(notification, uuid);
        if (active) {
            disableNotification(player, notification);
        } else {
            enableNotification(player, notification);
        }
    }

    private static void sendHelpMessage(Player player) {
        player.sendMessage(Component.translatable("nucleo.notifications.command.usage"));
    }
}