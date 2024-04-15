package de.keeeks.nucleo.modules.notifications.api.packet;

import de.keeeks.nucleo.modules.notifications.api.Notification;

public class NotificationCreatePacket extends NotificationPacket {
    public NotificationCreatePacket(Notification notification) {
        super(notification);
    }
}