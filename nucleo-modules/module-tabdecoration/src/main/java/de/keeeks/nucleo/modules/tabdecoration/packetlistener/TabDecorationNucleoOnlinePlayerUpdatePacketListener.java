package de.keeeks.nucleo.modules.tabdecoration.packetlistener;

import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerUpdatePacket;
import de.keeeks.nucleo.modules.tabdecoration.service.TabDecorationService;
import io.nats.client.Message;

@ListenerChannel(PlayerService.CHANNEL)
public class TabDecorationNucleoOnlinePlayerUpdatePacketListener extends PacketListener<NucleoOnlinePlayerUpdatePacket> {
    private final TabDecorationService tabDecorationService = ServiceRegistry.service(TabDecorationService.class);

    private final ProxyServer proxyServer;

    public TabDecorationNucleoOnlinePlayerUpdatePacketListener(ProxyServer proxyServer) {
        super(NucleoOnlinePlayerUpdatePacket.class);
        this.proxyServer = proxyServer;
    }

    @Override
    public void receive(
            NucleoOnlinePlayerUpdatePacket nucleoOnlinePlayerUpdatePacket,
            Message message
    ) {
        proxyServer.getAllPlayers().forEach(tabDecorationService::sendPlayerListHeaderAndFooter);
    }
}