package de.keeeks.nucleo.modules.players.proxy.packet.listener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.NucleoMessageSender;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.shared.packet.player.message.NucleoOnlinePlayerMessagePacket;
import io.nats.client.Message;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@ListenerChannel(PlayerService.CHANNEL)
public class NucleoOnlinePlayerMessagePacketListener extends PacketListener<NucleoOnlinePlayerMessagePacket> {
    private final BungeeAudiences bungeeAudiences;

    public NucleoOnlinePlayerMessagePacketListener(BungeeAudiences bungeeAudiences) {
        super(NucleoOnlinePlayerMessagePacket.class);
        this.bungeeAudiences = bungeeAudiences;
    }

    @Override
    public void receive(
            NucleoOnlinePlayerMessagePacket nucleoOnlinePlayerMessagePacket,
            Message message
    ) {
        ProxiedPlayer receiver = ProxyServer.getInstance().getPlayer(nucleoOnlinePlayerMessagePacket.receiverId());
        if (receiver == null || !receiver.isConnected()) return;

        NucleoMessageSender.MessageType messageType = nucleoOnlinePlayerMessagePacket.messageType();
        Audience audience = bungeeAudiences.player(receiver);
        if (messageType == NucleoMessageSender.MessageType.CHAT) {
            audience.sendMessage(nucleoOnlinePlayerMessagePacket.component());
        } else if (messageType == NucleoMessageSender.MessageType.ACTIONBAR) {
            audience.sendActionBar(nucleoOnlinePlayerMessagePacket.component());
        }
    }
}
