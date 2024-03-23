package de.keeeks.nucleo.modules.teamchat.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class TeamChatMessagePacket extends Packet {
    private final String message;
    private final UUID uuid;
    private final String senderServer;
    private final String senderProxy;
}