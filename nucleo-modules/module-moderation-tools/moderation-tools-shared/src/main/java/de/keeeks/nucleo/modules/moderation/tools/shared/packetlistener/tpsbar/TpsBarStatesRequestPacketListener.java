package de.keeeks.nucleo.modules.moderation.tools.shared.packetlistener.tpsbar;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.moderation.tools.shared.tpsbar.NucleoTpsBarApi;
import de.keeeks.nucleo.modules.moderation.tools.tpsbar.TpsBarApi;
import de.keeeks.nucleo.modules.moderation.tools.tpsbar.packet.TpsBarStatesRequestPacket;
import de.keeeks.nucleo.modules.moderation.tools.tpsbar.packet.TpsBarStatesResponsePacket;
import io.nats.client.Message;

@ListenerChannel(TpsBarApi.CHANNEL)
public final class TpsBarStatesRequestPacketListener extends PacketListener<TpsBarStatesRequestPacket> {
    private final NucleoTpsBarApi tpsBarApi;

    public TpsBarStatesRequestPacketListener(NucleoTpsBarApi tpsBarApi) {
        super(TpsBarStatesRequestPacket.class);
        this.tpsBarApi = tpsBarApi;
    }

    @Override
    public void receive(TpsBarStatesRequestPacket tpsBarStatesRequestPacket, Message message) {
        reply(message, new TpsBarStatesResponsePacket(tpsBarApi.enabledPlayers()));
    }
}