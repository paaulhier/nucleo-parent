package de.keeeks.nucleo.modules.common.commands.proxy.packet.listener.ping;

import de.keeeks.nucleo.modules.common.commands.proxy.packet.ping.PlayerPingRequestPacket;
import de.keeeks.nucleo.modules.common.commands.proxy.packet.ping.PlayerPingResponsePacket;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import io.nats.client.Message;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@ListenerChannel("common-commands")
public class PlayerPingRequestPacketListener extends PacketListener<PlayerPingRequestPacket> {
    public PlayerPingRequestPacketListener() {
        super(PlayerPingRequestPacket.class);
    }

    @Override
    public void receive(
            PlayerPingRequestPacket playerPingRequestPacket,
            Message message
    ) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerPingRequestPacket.uuid());
        if (player == null) return;

        reply(
                message,
                new PlayerPingResponsePacket(
                        playerPingRequestPacket.uuid(),
                        player.getPing()
                )
        );
    }
}