package de.keeeks.nucleo.modules.notifications.api.packet.user;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class NotificationUserUpdateStatePacket extends Packet {
    private final Notification notification;
    private final UUID affectedUser;
    private final boolean state;
}