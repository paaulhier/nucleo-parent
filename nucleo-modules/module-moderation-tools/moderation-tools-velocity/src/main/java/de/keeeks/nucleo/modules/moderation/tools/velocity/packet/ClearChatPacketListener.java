package de.keeeks.nucleo.modules.moderation.tools.velocity.packet;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.lejet.api.NameColorizer;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.moderation.tools.chatclear.packet.ClearChatPacket;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import net.kyori.adventure.text.Component;

import java.util.UUID;

import static net.kyori.adventure.text.Component.translatable;

public abstract class ClearChatPacketListener<P extends ClearChatPacket> extends PacketListener<P> {
    protected final NotificationApi notificationApi = ServiceRegistry.service(NotificationApi.class);
    protected final Notification notification = notificationApi.createNotification(
            "chatclear",
            "Notifys about chat clearings",
            "nucleo.notification.chatclear"
    );

    public ClearChatPacketListener(Class<P> packetClass) {
        super(packetClass);
    }

    protected final void clearChat(Player player, UUID executor) {
        if (player.hasPermission(notification.requiredPermission())) {
            sendNotification(player, executor);
            return;
        }
        for (int i = 0; i < 1000; i++) player.sendMessage(Component.empty());
        player.sendMessage(translatable(
                "nucleo.moderation.chatClear",
                NameColorizer.coloredName(executor)
        ));
    }

    private void sendNotification(Player player, UUID executor) {
        if (!notificationApi.notificationActive(notification, player.getUniqueId())) return;
        player.sendMessage(translatable(
                "nucleo.moderation.chatClear.notification",
                NameColorizer.coloredName(executor)
        ));
    }
}