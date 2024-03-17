package de.keeeks.nucleo.modules.players.shared.packet.player;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class NucleoPlayerInvalidatePacket extends Packet {
    private final UUID uuid;
}