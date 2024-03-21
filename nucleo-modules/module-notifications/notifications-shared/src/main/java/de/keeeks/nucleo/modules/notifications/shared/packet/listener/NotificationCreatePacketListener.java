package de.keeeks.nucleo.modules.notifications.shared.packet.listener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import de.keeeks.nucleo.modules.notifications.shared.NucleoNotificationApi;
import de.keeeks.nucleo.modules.notifications.shared.packet.NotificationCreatePacket;
import io.nats.client.Message;

@ListenerChannel(NotificationApi.CHANNEL)
public class NotificationCreatePacketListener extends NotificationPacketListener<NotificationCreatePacket> {

    public NotificationCreatePacketListener(NucleoNotificationApi notificationApi) {
        super(NotificationCreatePacket.class, notificationApi);
    }

    @Override
    public void receive(NotificationCreatePacket notificationCreatePacket, Message message) {
        notificationApi.modifyNotifications(list -> list.add(notificationCreatePacket.notification()));
    }
}