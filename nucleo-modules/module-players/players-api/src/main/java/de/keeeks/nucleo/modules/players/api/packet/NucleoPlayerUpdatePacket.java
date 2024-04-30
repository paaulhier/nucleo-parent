package de.keeeks.nucleo.modules.players.api.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NucleoPlayerUpdatePacket extends Packet {
    private final NucleoPlayer nucleoPlayer;
    private final String senderServiceName;
}