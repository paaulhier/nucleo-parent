package de.keeeks.nucleo.modules.notifications.spigot.vulcan;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import me.frep.vulcan.api.check.Check;
import me.frep.vulcan.api.event.VulcanFlagEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class NucleoVulcanNotificationListener implements Listener {
    private final NotificationApi notificationApi = ServiceRegistry.service(NotificationApi.class);
    private final Notification vulcanNotification = notificationApi.createNotification(
            "anticheat",
            "A notification from Vulcan if a player might be cheating"
    );


    @EventHandler
    public void handleFlag(VulcanFlagEvent event) {
        Player player = event.getPlayer();
        Check check = event.getCheck();

        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            if (!otherPlayer.hasPermission("nucleo.notifications.vulcan")) continue;
            if (!notificationApi.notificationActive(vulcanNotification, otherPlayer.getUniqueId())) continue;

            otherPlayer.sendMessage(translatable(
                    "nucleo.notifications.vulcan.flag",
                    text(player.getName()),
                    text(check.getName()),
                    text(check.getVl()),
                    text(check.getMaxVl()),
                    text(check.getCategory()),
                    text(check.getDisplayName()),
                    text(event.getInfo()),
                    text(check.getType())
            ));
        }
    }
}