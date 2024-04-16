package de.keeeks.nucleo.modules.notifications.shared.packet.listener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import de.keeeks.nucleo.modules.notifications.api.packet.NotificationDeletePacket;
import de.keeeks.nucleo.modules.notifications.shared.NucleoNotificationApi;
import io.nats.client.Message;

@ListenerChannel(NotificationApi.CHANNEL)
public class NotificationDeletePacketListener extends NotificationPacketListener<NotificationDeletePacket> {
    public NotificationDeletePacketListener(NucleoNotificationApi notificationApi) {
        super(NotificationDeletePacket.class, notificationApi);
    }

    @Override
    public void receive(NotificationDeletePacket notificationDeletePacket, Message message) {
        notificationApi.modifyNotifications(list -> list.remove(notificationDeletePacket.notification()));
    }
}
