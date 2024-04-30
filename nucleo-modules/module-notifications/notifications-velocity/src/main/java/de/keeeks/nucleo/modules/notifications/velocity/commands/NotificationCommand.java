package de.keeeks.nucleo.modules.notifications.velocity.commands;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.CommandHandlerVisitor;
import revxrsal.commands.annotation.*;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static net.kyori.adventure.text.Component.translatable;

@Command({"notify", "notification", "notifications"})
@CommandPermission("nucleo.commands.notifications")
public final class NotificationCommand implements CommandHandlerVisitor {
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
        Stream<Notification> notifications = notificationApi.notifications().stream().filter(
                notification -> {
                    if (notification.requiredPermission() == null) return true;
                    return player.hasPermission(notification.requiredPermission());
                }
        );

        if (notificationStateType == NotificationStateType.DISABLED) {
            notifications = notifications.filter(
                    notification -> !notificationApi.notificationActive(notification, player.getUniqueId())
            );
        } else if (notificationStateType == NotificationStateType.ENABLED) {
            notifications = notifications.filter(
                    notification -> notificationApi.notificationActive(notification, player.getUniqueId())
            );
        }

        List<Notification> filteredNotifications = notifications.toList();
        if (filteredNotifications.isEmpty()) {
            player.sendMessage(translatable("nucleo.notifications.command.list.empty"));
            return;
        }

        player.sendMessage(translatable("nucleo.notifications.command.list.header"));
        for (Notification notification : filteredNotifications) {
            player.sendMessage(translatable(
                    "nucleo.notifications.command.list.entry",
                    Component.text(notification.id()),
                    Component.text(notification.name()),
                    Component.text(notification.description())
            ));
        }
    }

    @Override
    public void visit(@NotNull CommandHandler commandHandler) {
        commandHandler.register(
                new DisableCommand(),
                new EnableCommand(),
                new ToggleCommand()
        );
    }

    @Command({"notify disable", "notification disable", "notifications disable"})
    public class DisableCommand {

        @AutoComplete("@notifications")
        @DefaultFor("~")
        public void disableCommand(Player player, Notification notification) {
            if (notification == null) {
                player.sendMessage(translatable("nucleo.notifications.command.noNotification"));
                return;
            }
            if (!checkPermission(player, notification)) {
                player.sendMessage(translatable(
                        "nucleo.notifications.command.toggle.noPermission",
                        Component.text(notification.name())
                ));
                return;
            }

            if (notificationApi.notificationActive(notification, player.getUniqueId())) {
                disableNotification(player, notification);
            } else {
                player.sendMessage(translatable(
                        "nucleo.notifications.command.disable.alreadyDisabled",
                        Component.text(notification.name())
                ));
            }
        }

        @Subcommand("all")
        public void disableAllCommand(Player player) {
            List<Notification> notifications = notificationApi.notifications().stream().filter(
                    notification -> checkPermission(player, notification)
            ).toList();
            for (Notification notification : notifications) {
                disableNotification(player, notification);
            }
        }
    }

    @Command({"notify enable", "notification enable", "notifications enable"})
    public class EnableCommand {

        @AutoComplete("@notifications")
        @DefaultFor("~")
        public void enableCommand(Player player, Notification notification) {
            if (notification == null) {
                player.sendMessage(translatable("nucleo.notifications.command.noNotification"));
                return;
            }
            if (!checkPermission(player, notification)) {
                player.sendMessage(translatable(
                        "nucleo.notifications.command.toggle.noPermission",
                        Component.text(notification.name())
                ));
                return;
            }

            if (!notificationApi.notificationActive(notification, player.getUniqueId())) {
                enableNotification(player, notification);
            } else {
                player.sendMessage(translatable(
                        "nucleo.notifications.command.enable.alreadyEnabled",
                        Component.text(notification.name())
                ));
            }
        }

        @Subcommand("all")
        public void disableAllCommand(Player player) {
            List<Notification> notifications = notificationApi.notifications().stream().filter(
                    notification -> checkPermission(player, notification)
            ).toList();
            for (Notification notification : notifications) {
                enableNotification(player, notification);
            }
        }
    }

    @Command({"notify toggle", "notification toggle", "notifications toggle"})
    public class ToggleCommand {

        @AutoComplete("@notifications")
        @DefaultFor("~")
        public void toggleCommand(Player player, Notification notification) {
            if (notification == null) {
                player.sendMessage(translatable("nucleo.notifications.command.noNotification"));
                return;
            }
            if (!checkPermission(player, notification)) {
                player.sendMessage(translatable(
                        "nucleo.notifications.command.toggle.noPermission",
                        Component.text(notification.name())
                ));
                return;
            }

            toggleNotification(player, notification);
        }

        @Subcommand("all")
        public void toggleAllCommand(Player player) {
            List<Notification> notifications = notificationApi.notifications().stream().filter(
                    notification -> checkPermission(player, notification)
            ).toList();
            for (Notification notification : notifications) {
                toggleNotification(player, notification);
            }
        }

        private void toggleNotification(Player player, Notification notification) {
            if (!notificationApi.notificationActive(notification, player.getUniqueId())) {
                enableNotification(player, notification);
            } else {
                disableNotification(player, notification);
            }
        }
    }

    private static boolean checkPermission(Player player, Notification notification) {
        if (notification.requiredPermission() == null) return true;
        return player.hasPermission(notification.requiredPermission());
    }

    private void enableNotification(Player player, Notification notification) {
        UUID uuid = player.getUniqueId();
        if (notificationApi.notificationActive(notification, uuid)) {
            player.sendMessage(translatable(
                    "nucleo.notifications.command.enable.alreadyEnabled",
                    Component.text(notification.name())
            ));
            return;
        }
        changeStateWithoutCheck(
                notification,
                uuid,
                true,
                player,
                "nucleo.notifications.command.enable"
        );
    }

    private void disableNotification(Player player, Notification notification) {
        UUID uuid = player.getUniqueId();
        if (!notificationApi.notificationActive(notification, uuid)) {
            player.sendMessage(translatable(
                    "nucleo.notifications.command.disable.alreadyDisabled",
                    Component.text(notification.name())
            ));
            return;
        }
        changeStateWithoutCheck(
                notification,
                uuid,
                false,
                player,
                "nucleo.notifications.command.disable"
        );
    }

    private void changeStateWithoutCheck(
            Notification notification,
            UUID uuid,
            boolean active,
            Player player,
            String key
    ) {
        notificationApi.notificationActive(notification, uuid, active);
        player.sendMessage(translatable(
                key,
                Component.text(notification.name())
        ));
    }

    private static void sendHelpMessage(Player player) {
        player.sendMessage(translatable("nucleo.notifications.command.usage"));
    }
}