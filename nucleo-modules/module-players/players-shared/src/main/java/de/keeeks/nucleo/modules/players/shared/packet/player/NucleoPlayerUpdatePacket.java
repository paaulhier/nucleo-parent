package de.keeeks.nucleo.modules.players.shared.packet.player;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class NucleoPlayerUpdatePacket extends Packet {
    private final NucleoPlayer nucleoPlayer;
}