package de.keeeks.nucleo.modules.players.shared.packet.player;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class NucleoPlayerInvalidatePacket extends Packet {
    private final UUID uuid;
}