package de.keeeks.nucleo.modules.vanish.shared.packetlistener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.vanish.api.VanishApi;
import de.keeeks.nucleo.modules.vanish.api.packet.VanishDataUpdatePacket;
import de.keeeks.nucleo.modules.vanish.shared.NucleoVanishApi;
import io.nats.client.Message;

@ListenerChannel(VanishApi.CHANNEL)
public class VanishDataUpdatePacketListener extends PacketListener<VanishDataUpdatePacket> {
    private final NucleoVanishApi vanishApi;

    public VanishDataUpdatePacketListener(NucleoVanishApi vanishApi) {
        super(VanishDataUpdatePacket.class);
        this.vanishApi = vanishApi;
    }

    @Override
    public void receive(
            VanishDataUpdatePacket vanishDataUpdatePacket,
            Message message
    ) {
        vanishApi.modifyVanishData(list -> {
            list.remove(vanishDataUpdatePacket.vanishData());
            list.add(vanishDataUpdatePacket.vanishData());
        });
    }
}