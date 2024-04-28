package de.keeeks.nucleo.modules.moderation.tools.shared.packetlistener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.cps.packet.ClickCheckInformationRequestPacket;
import de.keeeks.nucleo.modules.moderation.tools.cps.packet.ClickCheckInformationResponsePacket;
import io.nats.client.Message;

@ListenerChannel(ClickCheckApi.CHANNEL)
public class ClickCheckInformationRequestPacketListener extends PacketListener<ClickCheckInformationRequestPacket> {
    private final ClickCheckApi clickCheckApi;

    public ClickCheckInformationRequestPacketListener(ClickCheckApi clickCheckApi) {
        super(ClickCheckInformationRequestPacket.class);
        this.clickCheckApi = clickCheckApi;
    }

    @Override
    public void receive(
            ClickCheckInformationRequestPacket clickCheckInformationRequestPacket,
            Message message
    ) {
        reply(
                message,
                new ClickCheckInformationResponsePacket(clickCheckApi.clickChecks())
        );
    }
}