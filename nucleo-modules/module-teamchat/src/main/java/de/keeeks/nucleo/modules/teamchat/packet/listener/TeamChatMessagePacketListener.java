package de.keeeks.nucleo.modules.teamchat.packet.listener;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import de.keeeks.nucleo.modules.teamchat.packet.TeamChatMessagePacket;
import io.nats.client.Message;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import java.util.function.Function;

import static net.kyori.adventure.text.Component.translatable;

@ListenerChannel("nucleo:teamchat")
public class TeamChatMessagePacketListener extends PacketListener<TeamChatMessagePacket> {
    private final NotificationApi notificationApi = ServiceRegistry.service(NotificationApi.class);
    private final Notification notification = notificationApi.createNotification(
            "teamchat",
            "Notification to receive team chat messages"
    );

    private final ProxyServer proxyServer;
    private final Function<UUID, Component> playerNameApplier;

    public TeamChatMessagePacketListener(ProxyServer proxyServer, Function<UUID, Component> playerNameApplier) {
        super(TeamChatMessagePacket.class);
        this.proxyServer = proxyServer;
        this.playerNameApplier = playerNameApplier;
    }

    @Override
    public void receive(
            TeamChatMessagePacket teamChatMessagePacket,
            Message message
    ) {
        proxyServer.getAllPlayers().stream().filter(
                this::canReceiveTeamMessages
        ).forEach(player -> player.sendMessage(translatable(
                "nucleo.teamchat.message",
                playerNameApplier.apply(teamChatMessagePacket.uuid()),
                Component.text(teamChatMessagePacket.message()),
                Component.text(teamChatMessagePacket.senderServer()),
                Component.text(teamChatMessagePacket.senderProxy())
        )));
    }

    private boolean canReceiveTeamMessages(Player player) {
            return player.hasPermission("nucleo.teamchat.receive");
    }
}