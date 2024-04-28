package de.keeeks.nucleo.modules.tabdecoration.packetlistener;

import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.packet.NucleoPlayerUpdatePacket;
import de.keeeks.nucleo.modules.tabdecoration.service.TabDecorationService;
import io.nats.client.Message;

@ListenerChannel(PlayerService.CHANNEL)
public class TabDecorationNucleoPlayerUpdatePacketListener extends PacketListener<NucleoPlayerUpdatePacket> {
    private final TabDecorationService tabDecorationService = ServiceRegistry.service(TabDecorationService.class);

    private final ProxyServer proxyServer;

    public TabDecorationNucleoPlayerUpdatePacketListener(ProxyServer proxyServer) {
        super(NucleoPlayerUpdatePacket.class);
        this.proxyServer = proxyServer;
    }

    @Override
    public void receive(
            NucleoPlayerUpdatePacket nucleoPlayerUpdatePacket,
            Message message
    ) {
        proxyServer.getAllPlayers().forEach(tabDecorationService::sendPlayerListHeaderAndFooter);
    }
}