package de.keeeks.nucleo.modules.moderation.tools.cps.packet;

import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckInformation;

public class ClickCheckInformationDeletePacket extends ClickCheckInformationPacket {
    public ClickCheckInformationDeletePacket(ClickCheckInformation clickCheckInformation) {
        super(clickCheckInformation);
    }
}