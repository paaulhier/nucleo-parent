package de.keeeks.nucleo.modules.players.velocity.packet.listener;

import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.NucleoMessageSender;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.packet.message.NucleoOnlinePlayerMessagePacket;
import io.nats.client.Message;

@ListenerChannel(PlayerService.CHANNEL)
public class NucleoOnlinePlayerMessagePacketListener extends PacketListener<NucleoOnlinePlayerMessagePacket> {

    private final ProxyServer proxyServer;

    public NucleoOnlinePlayerMessagePacketListener(ProxyServer proxyServer) {
        super(NucleoOnlinePlayerMessagePacket.class);
        this.proxyServer = proxyServer;
    }

    @Override
    public void receive(
            NucleoOnlinePlayerMessagePacket nucleoOnlinePlayerMessagePacket,
            Message message
    ) {
        NucleoMessageSender.MessageType messageType = nucleoOnlinePlayerMessagePacket.messageType();

        proxyServer.getPlayer(nucleoOnlinePlayerMessagePacket.receiverId()).ifPresent(player -> {
            if (messageType == NucleoMessageSender.MessageType.CHAT) {
                player.sendMessage(nucleoOnlinePlayerMessagePacket.component());
            } else if (messageType == NucleoMessageSender.MessageType.ACTIONBAR) {
                player.sendActionBar(nucleoOnlinePlayerMessagePacket.component());
            }
        });
    }
}
