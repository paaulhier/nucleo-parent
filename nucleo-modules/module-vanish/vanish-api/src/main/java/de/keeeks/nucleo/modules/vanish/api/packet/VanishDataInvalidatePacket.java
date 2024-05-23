package de.keeeks.nucleo.modules.vanish.api.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.vanish.api.VanishData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VanishDataInvalidatePacket extends Packet {
    private final VanishData vanishData;
}