package de.keeeks.nucleo.modules.tabdecoration.packetlistener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.tabdecoration.service.TabDecorationService;
import de.keeeks.nucleo.syncproxy.api.SyncProxyService;
import de.keeeks.nucleo.syncproxy.api.packet.SyncProxyConfigurationUpdatePacket;
import io.nats.client.Message;
import org.bukkit.Bukkit;

@ListenerChannel(SyncProxyService.CHANNEL)
public final class TabDecorationSyncProxyConfigurationUpdatePacketListener extends PacketListener<SyncProxyConfigurationUpdatePacket> {
    private final TabDecorationService tabDecorationService = ServiceRegistry.service(TabDecorationService.class);

    public TabDecorationSyncProxyConfigurationUpdatePacketListener() {
        super(SyncProxyConfigurationUpdatePacket.class);
    }

    @Override
    public void receive(
            SyncProxyConfigurationUpdatePacket syncProxyConfigurationUpdatePacket,
            Message message
    ) {
        Bukkit.getOnlinePlayers().forEach(tabDecorationService::sendPlayerListHeaderAndFooter);
    }
}