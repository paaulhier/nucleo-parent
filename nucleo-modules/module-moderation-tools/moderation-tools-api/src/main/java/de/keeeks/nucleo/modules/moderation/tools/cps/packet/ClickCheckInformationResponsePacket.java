package de.keeeks.nucleo.modules.moderation.tools.cps.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckInformation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ClickCheckInformationResponsePacket extends Packet {
    private final List<ClickCheckInformation> clickCheckInformation;
}