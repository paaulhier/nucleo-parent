package de.keeeks.nucleo.modules.notifications.shared.packet;

import de.keeeks.nucleo.modules.notifications.api.Notification;

public class NotificationDeletePacket extends NotificationPacket {
    public NotificationDeletePacket(Notification notification) {
        super(notification);
    }
}