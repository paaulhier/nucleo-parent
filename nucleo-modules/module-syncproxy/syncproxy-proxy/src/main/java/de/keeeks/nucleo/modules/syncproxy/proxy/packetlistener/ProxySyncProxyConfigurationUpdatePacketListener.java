package de.keeeks.nucleo.modules.syncproxy.proxy.packetlistener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.syncproxy.proxy.configuration.SyncProxyKickScreenConfiguration;
import de.keeeks.nucleo.syncproxy.api.SyncProxyConfiguration;
import de.keeeks.nucleo.syncproxy.api.SyncProxyService;
import de.keeeks.nucleo.syncproxy.api.packet.SyncProxyConfigurationUpdatePacket;
import io.nats.client.Message;
import net.md_5.bungee.api.ProxyServer;

@ListenerChannel(SyncProxyService.CHANNEL)
public class ProxySyncProxyConfigurationUpdatePacketListener extends PacketListener<SyncProxyConfigurationUpdatePacket> {
    private final SyncProxyKickScreenConfiguration syncProxyKickScreenConfiguration;

    public ProxySyncProxyConfigurationUpdatePacketListener(
            SyncProxyKickScreenConfiguration syncProxyKickScreenConfiguration
    ) {
        super(SyncProxyConfigurationUpdatePacket.class);
        this.syncProxyKickScreenConfiguration = syncProxyKickScreenConfiguration;
    }

    @Override
    public void receive(
            SyncProxyConfigurationUpdatePacket syncProxyConfigurationUpdatePacket,
            Message message
    ) {
        SyncProxyConfiguration syncProxyConfiguration = syncProxyConfigurationUpdatePacket.syncProxyConfiguration();
        if (syncProxyConfiguration.active() && syncProxyConfiguration.maintenance()) {
            ProxyServer.getInstance().getPlayers().stream().filter(
                    player -> !player.hasPermission("nucleo.syncproxy.maintenance.bypass")
            ).forEach(
                    player -> player.disconnect(syncProxyKickScreenConfiguration.toBungeeComponent())
            );
        }
    }
}