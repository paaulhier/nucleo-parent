package de.keeeks.nucleo.modules.automessage.shared.packet;

import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;
import de.keeeks.nucleo.modules.messaging.packet.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AutomaticMessagePacket extends Packet {
    private final AutomaticMessage automaticMessage;
}