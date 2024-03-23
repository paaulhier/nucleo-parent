package de.keeeks.nucleo.modules.teamchat.commands;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.teamchat.packet.TeamChatMessagePacket;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.bungee.annotation.CommandPermission;

@Command({"teamchat", "tc"})
@CommandPermission("nucleo.commands.teamchat")
@RequiredArgsConstructor
public class TeamChatCommand {
    private final NatsConnection natsConnection;

    @DefaultFor({"teamchat", "tc"})
    public void teamChatCommand(
            Audience audience,
            @Optional String message
    ) {
        if (message == null) {
            audience.sendMessage(Component.translatable("nucleo.commands.teamchat.noMessage"));
            return;
        }
        audience.get(Identity.UUID).ifPresent(uuid -> {
            ServerInfo serverInfo = ProxyServer.getInstance().getPlayer(uuid).getServer().getInfo();
            natsConnection.publishPacket(
                    "nucleo:teamchat",
                    new TeamChatMessagePacket(
                            message,
                            uuid,
                            serverInfo.getName(),
                            Module.serviceName()
                    )
            );
        });
    }
}