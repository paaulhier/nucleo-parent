package de.keeeks.nucleo.modules.moderation.tools.shared.packet;

import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckInformation;

public class ClickCheckInformationDeletePacket extends ClickCheckInformationPacket{
    public ClickCheckInformationDeletePacket(ClickCheckInformation clickCheckInformation) {
        super(clickCheckInformation);
    }
}