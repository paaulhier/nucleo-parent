package de.keeeks.nucleo.modules.moderation.tools.shared.packet;

import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckInformation;

public class ClickCheckInformationUpdatePacket extends ClickCheckInformationPacket {
    public ClickCheckInformationUpdatePacket(ClickCheckInformation clickCheckInformation) {
        super(clickCheckInformation);
    }
}