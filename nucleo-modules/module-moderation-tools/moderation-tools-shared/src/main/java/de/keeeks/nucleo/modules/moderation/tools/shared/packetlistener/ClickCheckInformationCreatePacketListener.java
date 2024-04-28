package de.keeeks.nucleo.modules.moderation.tools.shared.packetlistener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.shared.cps.NucleoClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.cps.packet.ClickCheckInformationCreatePacket;
import io.nats.client.Message;

@ListenerChannel(ClickCheckApi.CHANNEL)
public class ClickCheckInformationCreatePacketListener extends PacketListener<ClickCheckInformationCreatePacket> {
    private final NucleoClickCheckApi nucleoClickCheckApi;

    public ClickCheckInformationCreatePacketListener(NucleoClickCheckApi nucleoClickCheckApi) {
        super(ClickCheckInformationCreatePacket.class);
        this.nucleoClickCheckApi = nucleoClickCheckApi;
    }

    @Override
    public void receive(
            ClickCheckInformationCreatePacket clickCheckInformationCreatePacket,
            Message message
    ) {
        nucleoClickCheckApi.cacheLocally(clickCheckInformationCreatePacket.clickCheckInformation());
    }
}