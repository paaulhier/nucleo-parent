package de.keeeks.nucleo.modules.moderation.tools.velocity.packet;

import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.moderation.tools.chatclear.ChatClearApi;
import de.keeeks.nucleo.modules.moderation.tools.chatclear.packet.ClearGlobalChatPacket;
import io.nats.client.Message;

@ListenerChannel(ChatClearApi.CHANNEL)
public class ClearGlobalChatPacketListener extends ClearChatPacketListener<ClearGlobalChatPacket> {
    private final ProxyServer proxyServer;

    public ClearGlobalChatPacketListener(ProxyServer proxyServer) {
        super(ClearGlobalChatPacket.class);
        this.proxyServer = proxyServer;
    }

    @Override
    public void receive(
            ClearGlobalChatPacket clearGlobalChatPacket,
            Message message
    ) {
        proxyServer.getAllServers().stream().flatMap(
                registeredServer -> registeredServer.getPlayersConnected().stream()
        ).forEach(player -> clearChat(
                player,
                clearGlobalChatPacket.executor()
        ));
    }
}