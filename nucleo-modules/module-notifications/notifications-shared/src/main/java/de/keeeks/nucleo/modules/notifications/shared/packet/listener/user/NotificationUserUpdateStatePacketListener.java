package de.keeeks.nucleo.modules.notifications.shared.packet.listener.user;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import de.keeeks.nucleo.modules.notifications.api.packet.user.NotificationUserUpdateStatePacket;
import de.keeeks.nucleo.modules.notifications.shared.NucleoNotificationApi;
import io.nats.client.Message;

@ListenerChannel(NotificationApi.CHANNEL)
public class NotificationUserUpdateStatePacketListener extends PacketListener<NotificationUserUpdateStatePacket> {
    private final NucleoNotificationApi notificationApi;

    public NotificationUserUpdateStatePacketListener(NucleoNotificationApi notificationApi) {
        super(NotificationUserUpdateStatePacket.class);
        this.notificationApi = notificationApi;
    }

    @Override
    public void receive(
            NotificationUserUpdateStatePacket notificationUserUpdateStatePacket,
            Message message
    ) {
        notificationApi.modifyState(
                notificationUserUpdateStatePacket.notification(),
                notificationUserUpdateStatePacket.affectedUser(),
                notificationUserUpdateStatePacket.state()
        );
    }
}