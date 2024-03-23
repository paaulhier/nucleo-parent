package de.keeeks.nucleo.modules.common.commands.proxy.commands.team;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bungee.BungeeCommandActor;
import revxrsal.commands.bungee.annotation.CommandPermission;

@Command({"jumpto", "jt", "goto"})
@CommandPermission("nucleo.commands.jumpto")
@RequiredArgsConstructor
public class JumpToCommand {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);

    private final BungeeAudiences bungeeAudiences;

    @AutoComplete("players")
    @Usage("jumpto <player>")
    @DefaultFor({"jumpto", "jt", "goto"})
    public void jumpToCommand(
            BungeeCommandActor actor,
            String playerName
    ) {
        ProxiedPlayer player = actor.requirePlayer();
        Audience audience = bungeeAudiences.player(player);

        playerService.onlinePlayer(playerName).ifPresentOrElse(nucleoOnlinePlayer -> {
            ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(nucleoOnlinePlayer.server());
            if (serverInfo == null) {
                audience.sendMessage(Component.translatable("nucleo.commands.jumpto.unknownServer").arguments(
                        Component.text(playerName)
                ));
                return;
            }
            player.connect(
                    serverInfo,
                    (aBoolean, throwable) -> {
                        if (!aBoolean || throwable != null) {
                            audience.sendMessage(Component.translatable("nucleo.commands.jumpto.failed").arguments(
                                    Component.text(playerName)
                            ));
                            return;
                        }
                        audience.sendMessage(Component.translatable("nucleo.commands.jumpto.success").arguments(
                                Component.text(playerName)
                        ));
                    },
                    ServerConnectEvent.Reason.PLUGIN
            );
        }, () -> audience.sendMessage(Component.translatable("playerNotFound").arguments(
                Component.text(playerName)
        )));
    }

}