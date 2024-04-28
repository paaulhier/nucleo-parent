package de.keeeks.nucleo.modules.tabdecoration.packetlistener;

import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.tabdecoration.service.TabDecorationService;
import de.keeeks.nucleo.syncproxy.api.SyncProxyService;
import de.keeeks.nucleo.syncproxy.api.packet.SyncProxyConfigurationUpdatePacket;
import io.nats.client.Message;

@ListenerChannel(SyncProxyService.CHANNEL)
public class TabDecorationSyncProxyConfigurationUpdatePacketListener extends PacketListener<SyncProxyConfigurationUpdatePacket> {
    private final TabDecorationService tabDecorationService = ServiceRegistry.service(TabDecorationService.class);

    private final ProxyServer proxyServer;

    public TabDecorationSyncProxyConfigurationUpdatePacketListener(ProxyServer proxyServer) {
        super(SyncProxyConfigurationUpdatePacket.class);
        this.proxyServer = proxyServer;
    }

    @Override
    public void receive(
            SyncProxyConfigurationUpdatePacket syncProxyConfigurationUpdatePacket,
            Message message
    ) {
        proxyServer.getAllPlayers().forEach(tabDecorationService::sendPlayerListHeaderAndFooter);
    }
}