package de.keeeks.nucleo.modules.syncproxy.velocity.packetlistener;

import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.syncproxy.api.SyncProxyConfiguration;
import de.keeeks.nucleo.syncproxy.api.SyncProxyService;
import de.keeeks.nucleo.syncproxy.api.packet.SyncProxyConfigurationUpdatePacket;
import io.nats.client.Message;

import static net.kyori.adventure.text.Component.translatable;

@ListenerChannel(SyncProxyService.CHANNEL)
public class ProxySyncProxyConfigurationUpdatePacketListener extends PacketListener<SyncProxyConfigurationUpdatePacket> {
    private final ProxyServer proxyServer;

    public ProxySyncProxyConfigurationUpdatePacketListener(ProxyServer proxyServer) {
        super(SyncProxyConfigurationUpdatePacket.class);
        this.proxyServer = proxyServer;
    }

    @Override
    public void receive(
            SyncProxyConfigurationUpdatePacket syncProxyConfigurationUpdatePacket,
            Message message
    ) {
        SyncProxyConfiguration syncProxyConfiguration = syncProxyConfigurationUpdatePacket.syncProxyConfiguration();
        if (syncProxyConfiguration.active() && syncProxyConfiguration.maintenance()) {
            proxyServer.getAllPlayers().stream().filter(
                    player -> !player.hasPermission("nucleo.syncproxy.maintenance.bypass")
            ).forEach(
                    player -> player.disconnect(translatable("nucleo.syncproxy.maintenance.kick"))
            );
        }
    }
}