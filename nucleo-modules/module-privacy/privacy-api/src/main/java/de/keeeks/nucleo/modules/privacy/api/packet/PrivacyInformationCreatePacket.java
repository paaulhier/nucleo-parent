package de.keeeks.nucleo.modules.privacy.api.packet;

import de.keeeks.nucleo.modules.privacy.api.PrivacyInformation;

public class PrivacyInformationCreatePacket extends PrivacyInformationUpdatePacket{
    public PrivacyInformationCreatePacket(PrivacyInformation privacyInformation) {
        super(privacyInformation);
    }
}