package de.keeeks.nucleo.modules.moderation.tools.velocity.packet;

import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.moderation.tools.chatclear.ChatClearApi;
import de.keeeks.nucleo.modules.moderation.tools.chatclear.packet.ClearServerChatPacket;
import io.nats.client.Message;

import static net.kyori.adventure.text.Component.text;

@ListenerChannel(ChatClearApi.CHANNEL)
public class ClearServerChatPacketListener extends ClearChatPacketListener<ClearServerChatPacket> {
    private final ProxyServer proxyServer;

    public ClearServerChatPacketListener(ProxyServer proxyServer) {
        super(ClearServerChatPacket.class);
        this.proxyServer = proxyServer;
    }

    @Override
    public void receive(
            ClearServerChatPacket clearServerChatPacket,
            Message message
    ) {
        proxyServer.getServer(clearServerChatPacket.server()).stream().flatMap(
                registeredServer -> registeredServer.getPlayersConnected().stream()
        ).forEach(player -> clearChat(
                player,
                clearServerChatPacket.executor(),
                ClearChatType.SERVER,
                text(clearServerChatPacket.server())
        ));
    }
}