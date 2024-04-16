package de.keeeks.nucleo.modules.notifications.shared.packet.listener;

import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.notifications.api.packet.NotificationPacket;
import de.keeeks.nucleo.modules.notifications.shared.NucleoNotificationApi;

public abstract class NotificationPacketListener<P extends NotificationPacket> extends PacketListener<P> {
    protected final NucleoNotificationApi notificationApi;

    public NotificationPacketListener(Class<P> packetClass, NucleoNotificationApi notificationApi) {
        super(packetClass);
        this.notificationApi = notificationApi;
    }
}