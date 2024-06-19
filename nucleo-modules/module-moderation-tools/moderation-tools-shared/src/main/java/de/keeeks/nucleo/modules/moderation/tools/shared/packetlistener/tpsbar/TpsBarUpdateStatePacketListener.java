package de.keeeks.nucleo.modules.moderation.tools.shared.packetlistener.tpsbar;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.moderation.tools.shared.tpsbar.NucleoTpsBarApi;
import de.keeeks.nucleo.modules.moderation.tools.tpsbar.TpsBarApi;
import de.keeeks.nucleo.modules.moderation.tools.tpsbar.packet.TpsBarUpdateStatePacket;
import io.nats.client.Message;

@ListenerChannel(TpsBarApi.CHANNEL)
public final class TpsBarUpdateStatePacketListener extends PacketListener<TpsBarUpdateStatePacket> {
    private final NucleoTpsBarApi tpsBarApi;

    public TpsBarUpdateStatePacketListener(NucleoTpsBarApi tpsBarApi) {
        super(TpsBarUpdateStatePacket.class);
        this.tpsBarApi = tpsBarApi;
    }

    @Override
    public void receive(TpsBarUpdateStatePacket tpsBarUpdateStatePacket, Message message) {
        if (tpsBarUpdateStatePacket.enabled()) {
            tpsBarApi.modifyEnabledPlayers(modifier -> modifier.add(tpsBarUpdateStatePacket.uuid()));
        } else {
            tpsBarApi.modifyEnabledPlayers(modifier -> modifier.remove(tpsBarUpdateStatePacket.uuid()));
        }
    }
}