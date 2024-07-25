package de.keeeks.nucleo.modules.tabdecoration.packetlistener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.packet.NucleoPlayerUpdatePacket;
import de.keeeks.nucleo.modules.tabdecoration.service.TabDecorationService;
import io.nats.client.Message;
import org.bukkit.Bukkit;

@ListenerChannel(PlayerService.CHANNEL)
public final class TabDecorationNucleoPlayerUpdatePacketListener extends PacketListener<NucleoPlayerUpdatePacket> {
    private final TabDecorationService tabDecorationService = ServiceRegistry.service(TabDecorationService.class);


    public TabDecorationNucleoPlayerUpdatePacketListener() {
        super(NucleoPlayerUpdatePacket.class);
    }

    @Override
    public void receive(
            NucleoPlayerUpdatePacket nucleoPlayerUpdatePacket,
            Message message
    ) {
        Bukkit.getOnlinePlayers().forEach(tabDecorationService::sendPlayerListHeaderAndFooter);
    }
}