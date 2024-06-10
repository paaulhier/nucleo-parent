package de.keeeks.nucleo.modules.moderation.tools.broadcast.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.moderation.tools.broadcast.BroadcastOptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

@Getter
@RequiredArgsConstructor
public class BroadcastPacket extends Packet {
    private final Component message;
    private final BroadcastOptions options;
}