package de.keeeks.nucleo.modules.teamchat.commands;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.ServerInfo;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.teamchat.packet.TeamChatMessagePacket;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.velocity.annotation.CommandPermission;

@Command({"teamchat", "tc"})
@CommandPermission("nucleo.commands.teamchat")
@RequiredArgsConstructor
public class TeamChatCommand {
    private final NatsConnection natsConnection;
    private final ProxyServer proxyServer;

    @DefaultFor({"teamchat", "tc"})
    public void teamChatCommand(
            Player player,
            @Optional String message
    ) {
        if (message == null) {
            player.sendMessage(Component.translatable("nucleo.commands.teamchat.noMessage"));
            return;
        }
        String serverName = player.getCurrentServer().map(
                ServerConnection::getServerInfo
        ).map(
                ServerInfo::getName
        ).orElse("Unknown");
        natsConnection.publishPacket(
                "nucleo:teamchat",
                new TeamChatMessagePacket(
                        message,
                        player.getUniqueId(),
                        serverName,
                        Module.serviceName()
                )
        );
    }
}