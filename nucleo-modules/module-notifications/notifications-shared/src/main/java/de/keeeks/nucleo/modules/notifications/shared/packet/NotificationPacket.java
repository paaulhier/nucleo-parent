package de.keeeks.nucleo.modules.notifications.shared.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationPacket extends Packet {
    private final Notification notification;
}