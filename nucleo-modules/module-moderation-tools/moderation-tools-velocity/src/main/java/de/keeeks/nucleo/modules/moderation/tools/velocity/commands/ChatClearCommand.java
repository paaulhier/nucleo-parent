package de.keeeks.nucleo.modules.moderation.tools.velocity.commands;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.moderation.tools.chatclear.ChatClearApi;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import lombok.RequiredArgsConstructor;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.velocity.annotation.CommandPermission;

import static net.kyori.adventure.text.Component.translatable;

@Command({"chatclear", "cc"})
@CommandPermission("nucleo.moderation.chatclear")
@RequiredArgsConstructor
public class ChatClearCommand {
    private final ChatClearApi chatClearApi = ServiceRegistry.service(ChatClearApi.class);
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);

    private final ProxyServer proxyServer;

    @DefaultFor({"chatclear", "cc"})
    public void chatClearCommand(Player player, @Optional String serverOrPlayer) {
        if (serverOrPlayer == null) {
            player.getCurrentServer().ifPresent(serverConnection -> chatClearApi.clearChat(
                    serverConnection.getServerInfo().getName(),
                    player.getUniqueId()
            ));
            return;
        }

        if (serverOrPlayer.equalsIgnoreCase("global") && player.hasPermission(
                "nucleo.moderation.chatclear.global"
        )) {
            chatClearApi.clearGlobalChat(player.getUniqueId());
            return;
        }

        proxyServer.getServer(serverOrPlayer).ifPresentOrElse(registeredServer -> chatClearApi.clearChat(
                registeredServer.getServerInfo().getName(),
                player.getUniqueId()
        ), () -> playerService.onlinePlayer(serverOrPlayer).ifPresentOrElse(
                onlinePlayer -> chatClearApi.clearChat(
                        onlinePlayer.uuid(),
                        player.getUniqueId()
                ), () -> player.sendMessage(translatable(
                        "nucleo.moderation.chatclear.playerOrServerNotFound"
                ))
        ));
    }
}