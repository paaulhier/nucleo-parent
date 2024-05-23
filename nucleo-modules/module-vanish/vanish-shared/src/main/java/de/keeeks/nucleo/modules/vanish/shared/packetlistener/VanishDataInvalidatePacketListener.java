package de.keeeks.nucleo.modules.vanish.shared.packetlistener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.vanish.api.VanishApi;
import de.keeeks.nucleo.modules.vanish.api.packet.VanishDataInvalidatePacket;
import de.keeeks.nucleo.modules.vanish.shared.NucleoVanishApi;
import io.nats.client.Message;

@ListenerChannel(VanishApi.CHANNEL)
public class VanishDataInvalidatePacketListener extends PacketListener<VanishDataInvalidatePacket> {
    private final NucleoVanishApi vanishApi;

    public VanishDataInvalidatePacketListener(NucleoVanishApi vanishApi) {
        super(VanishDataInvalidatePacket.class);
        this.vanishApi = vanishApi;
    }

    @Override
    public void receive(VanishDataInvalidatePacket vanishDataInvalidatePacket, Message message) {
        vanishApi.modifyVanishData(list -> list.remove(vanishDataInvalidatePacket.vanishData()));
    }
}