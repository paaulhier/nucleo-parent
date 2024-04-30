package de.keeeks.nucleo.modules.moderation.tools.shared.packetlistener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.cps.packet.ClickCheckInformationDeletePacket;
import de.keeeks.nucleo.modules.moderation.tools.shared.cps.NucleoClickCheckApi;
import io.nats.client.Message;

@ListenerChannel(ClickCheckApi.CHANNEL)
public class ClickCheckInformationDeletePacketListener extends PacketListener<ClickCheckInformationDeletePacket> {
    private final NucleoClickCheckApi nucleoClickCheckApi;

    public ClickCheckInformationDeletePacketListener(NucleoClickCheckApi nucleoClickCheckApi) {
        super(ClickCheckInformationDeletePacket.class);
        this.nucleoClickCheckApi = nucleoClickCheckApi;
    }

    @Override
    public void receive(
            ClickCheckInformationDeletePacket clickCheckInformationDeletePacket,
            Message message
    ) {
        nucleoClickCheckApi.deleteLocally(clickCheckInformationDeletePacket.clickCheckInformation());
    }
}