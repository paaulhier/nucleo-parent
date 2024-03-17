package de.keeeks.nucleo.modules.common.commands.proxy.packet.ping;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PlayerPingRequestPacket extends Packet {
    private final UUID uuid;
}