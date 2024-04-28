package de.keeeks.nucleo.modules.moderation.tools.velocity.packet;

import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.moderation.tools.chatclear.ChatClearApi;
import de.keeeks.nucleo.modules.moderation.tools.chatclear.packet.ClearPlayerChatPacket;
import io.nats.client.Message;

@ListenerChannel(ChatClearApi.CHANNEL)
public class ClearPlayerChatPacketListener extends ClearChatPacketListener<ClearPlayerChatPacket> {
    private final ProxyServer proxyServer;

    public ClearPlayerChatPacketListener(ProxyServer proxyServer) {
        super(ClearPlayerChatPacket.class);
        this.proxyServer = proxyServer;
    }

    @Override
    public void receive(
            ClearPlayerChatPacket clearPlayerChatPacket,
            Message message
    ) {
        proxyServer.getPlayer(clearPlayerChatPacket.uuid()).ifPresent(player -> clearChat(
                player,
                clearPlayerChatPacket.executor()
        ));
    }
}