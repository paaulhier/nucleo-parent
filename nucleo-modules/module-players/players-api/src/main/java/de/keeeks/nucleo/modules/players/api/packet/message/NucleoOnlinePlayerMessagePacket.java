package de.keeeks.nucleo.modules.players.api.packet.message;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.players.api.NucleoMessageSender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class NucleoOnlinePlayerMessagePacket extends Packet {
    private final UUID receiverId;
    private final Component component;
    private final NucleoMessageSender.MessageType messageType;

}