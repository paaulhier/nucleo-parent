package de.keeeks.nucleo.modules.moderation.tools.velocity.packetlistener;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.moderation.tools.broadcast.BroadcastApi;
import de.keeeks.nucleo.modules.moderation.tools.broadcast.BroadcastOptions;
import de.keeeks.nucleo.modules.moderation.tools.broadcast.packet.BroadcastPacket;
import io.nats.client.Message;

import static net.kyori.adventure.text.Component.translatable;

@ListenerChannel(BroadcastApi.CHANNEL)
public class BroadcastPacketListener extends PacketListener<BroadcastPacket> {
    private final ProxyServer proxyServer;

    public BroadcastPacketListener(ProxyServer proxyServer) {
        super(BroadcastPacket.class);
        this.proxyServer = proxyServer;
    }

    @Override
    public void receive(
            BroadcastPacket broadcastPacket,
            Message message
    ) {
        BroadcastOptions broadcastOptions = broadcastPacket.options();
        for (Player player : proxyServer.getAllPlayers()) {
            if (broadcastOptions.server() != null) {
                ServerConnection serverConnection = player.getCurrentServer().orElse(null);
                if (serverConnection == null) continue;
                if (!serverConnection.getServerInfo().getName().equals(broadcastOptions.server())) continue;
            }
            if (broadcastOptions.permission() != null) {
                if (!player.hasPermission(broadcastOptions.permission())) continue;
            }
            player.sendMessage(translatable(
                    "nucleo.moderation.tools.broadcast.message",
                    broadcastPacket.message()
            ));
        }
    }
}