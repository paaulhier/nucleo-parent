package de.keeeks.nucleo.modules.teamchat.packet.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import de.keeeks.nucleo.modules.teamchat.packet.TeamChatMessagePacket;
import io.nats.client.Message;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;
import java.util.function.Function;

@ListenerChannel("nucleo:teamchat")
public class TeamChatMessagePacketListener extends PacketListener<TeamChatMessagePacket> {
    private final NotificationApi notificationApi = ServiceRegistry.service(NotificationApi.class);
    private final Notification notification = notificationApi.createNotification(
            "teamchat",
            "Notification to receive team chat messages"
    );

    private final BungeeAudiences bungeeAudiences;
    private final Function<UUID, Component> playerNameApplier;

    public TeamChatMessagePacketListener(BungeeAudiences bungeeAudiences, Function<UUID, Component> playerNameApplier) {
        super(TeamChatMessagePacket.class);
        this.bungeeAudiences = bungeeAudiences;
        this.playerNameApplier = playerNameApplier;
    }

    @Override
    public void receive(
            TeamChatMessagePacket teamChatMessagePacket,
            Message message
    ) {
        bungeeAudiences.filter(
                this::canReceiveTeamMessages
        ).forEachAudience(audience -> audience.sendMessage(Component.translatable(
                "nucleo.teamchat.message",
                playerNameApplier.apply(teamChatMessagePacket.uuid()),
                Component.text(teamChatMessagePacket.message()),
                Component.text(teamChatMessagePacket.senderServer()),
                Component.text(teamChatMessagePacket.senderProxy())
        )));
    }

    private boolean canReceiveTeamMessages(CommandSender commandSender) {
        if (commandSender instanceof ProxiedPlayer player) {
            if (!commandSender.hasPermission("nucleo.teamchat.receive")) return false;
            return notificationApi.notificationActive(notification, player.getUniqueId());
        }
        return false;
    }
}