package de.keeeks.nucleo.modules.notifications.shared.packet.listener;

import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.notifications.shared.NucleoNotificationApi;
import de.keeeks.nucleo.modules.notifications.api.packet.NotificationPacket;

public abstract class NotificationPacketListener<P extends NotificationPacket> extends PacketListener<P> {
    protected final NucleoNotificationApi notificationApi;

    public NotificationPacketListener(Class<P> packetClass, NucleoNotificationApi notificationApi) {
        super(packetClass);
        this.notificationApi = notificationApi;
    }
}