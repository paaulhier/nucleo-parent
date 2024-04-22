package de.keeeks.nucleo.modules.privacy.api.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.privacy.api.PrivacyInformation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PrivacyInformationUpdatePacket extends Packet {
    private final PrivacyInformation privacyInformation;
}