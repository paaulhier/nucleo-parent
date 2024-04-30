package de.keeeks.nucleo.modules.moderation.tools.velocity.packet;

import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.lejet.api.NameColorizer;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.moderation.tools.chatclear.ChatClearApi;
import de.keeeks.nucleo.modules.moderation.tools.chatclear.packet.ClearPlayerChatPacket;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import io.nats.client.Message;

@ListenerChannel(ChatClearApi.CHANNEL)
public class ClearPlayerChatPacketListener extends ClearChatPacketListener<ClearPlayerChatPacket> {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);

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
        playerService.player(clearPlayerChatPacket.uuid()).ifPresent(
                nucleoPlayer -> proxyServer.getPlayer(clearPlayerChatPacket.uuid()).ifPresent(
                        player -> clearChat(
                                player,
                                clearPlayerChatPacket.executor(),
                                ClearChatType.PLAYER,
                                NameColorizer.coloredName(nucleoPlayer.uuid())
                        )
                )
        );
    }
}