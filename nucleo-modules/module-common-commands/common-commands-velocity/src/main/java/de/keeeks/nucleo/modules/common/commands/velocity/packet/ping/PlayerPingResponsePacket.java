package de.keeeks.nucleo.modules.common.commands.velocity.packet.ping;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PlayerPingResponsePacket extends Packet {
    private final UUID uuid;
    private final int ping;
}