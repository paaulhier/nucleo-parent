package de.keeeks.nucleo.modules.privacy.shared.packet.listener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.privacy.api.PrivacyApi;
import de.keeeks.nucleo.modules.privacy.shared.NucleoPrivacyApi;
import de.keeeks.nucleo.modules.privacy.api.packet.PrivacyInformationUpdatePacket;
import io.nats.client.Message;

@ListenerChannel(PrivacyApi.CHANNEL)
public class PrivacyInformationUpdatePacketListener extends PacketListener<PrivacyInformationUpdatePacket> {

    private final NucleoPrivacyApi privacyApi;

    public PrivacyInformationUpdatePacketListener(NucleoPrivacyApi privacyApi) {
        super(PrivacyInformationUpdatePacket.class);
        this.privacyApi = privacyApi;
    }

    @Override
    public void receive(PrivacyInformationUpdatePacket privacyInformationUpdatePacket, Message message) {
        privacyApi.modifyCache(cache -> cache.put(
                privacyInformationUpdatePacket.privacyInformation().playerId(),
                privacyInformationUpdatePacket.privacyInformation()
        ));
    }
}