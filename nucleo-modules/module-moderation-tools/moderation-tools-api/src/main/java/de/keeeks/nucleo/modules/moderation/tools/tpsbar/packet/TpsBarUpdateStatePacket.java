package de.keeeks.nucleo.modules.moderation.tools.tpsbar.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class TpsBarUpdateStatePacket extends Packet {
    private final UUID uuid;
    private final boolean enabled;
}