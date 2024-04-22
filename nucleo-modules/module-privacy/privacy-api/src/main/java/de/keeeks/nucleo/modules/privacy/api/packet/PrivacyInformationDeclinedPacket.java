package de.keeeks.nucleo.modules.privacy.api.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.privacy.api.PrivacyInformation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PrivacyInformationDeclinedPacket extends Packet {
    private final UUID playerId;
    private final PrivacyInformation privacyInformation;
}