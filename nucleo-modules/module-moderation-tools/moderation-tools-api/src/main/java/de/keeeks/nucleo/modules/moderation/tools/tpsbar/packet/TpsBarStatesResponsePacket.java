package de.keeeks.nucleo.modules.moderation.tools.tpsbar.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class TpsBarStatesResponsePacket extends Packet {
    private final List<UUID> enabledPlayers;
}