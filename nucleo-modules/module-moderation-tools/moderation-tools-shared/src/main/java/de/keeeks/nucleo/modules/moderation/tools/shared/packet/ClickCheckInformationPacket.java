package de.keeeks.nucleo.modules.moderation.tools.shared.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckInformation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ClickCheckInformationPacket extends Packet {
    private final ClickCheckInformation clickCheckInformation;
}