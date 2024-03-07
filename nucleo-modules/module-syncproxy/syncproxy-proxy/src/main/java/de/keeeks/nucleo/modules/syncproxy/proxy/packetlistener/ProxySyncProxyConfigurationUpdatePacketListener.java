package de.keeeks.nucleo.modules.syncproxy.proxy.packetlistener;

import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.syncproxy.proxy.configuration.SyncProxyKickScreenConfiguration;
import de.keeeks.nucleo.syncproxy.api.configuration.packet.SyncProxyConfigurationUpdatePacket;
import io.nats.client.Message;
import net.md_5.bungee.api.ProxyServer;

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
        ProxyServer.getInstance().getPlayers().forEach(
                player -> player.disconnect(syncProxyKickScreenConfiguration.toBungeeComponent())
        );
    }
}