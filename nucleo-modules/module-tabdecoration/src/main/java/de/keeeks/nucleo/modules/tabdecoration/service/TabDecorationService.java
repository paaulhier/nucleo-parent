package de.keeeks.nucleo.modules.tabdecoration.service;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import de.keeeks.lejet.api.permission.PermissionApi;
import de.keeeks.lejet.api.permission.PermissionUser;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.syncproxy.api.SyncProxyConfiguration;
import de.keeeks.nucleo.syncproxy.api.SyncProxyService;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Optional;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class TabDecorationService {
    private final SyncProxyService syncProxyService = ServiceRegistry.service(SyncProxyService.class);
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final PermissionApi permissionApi = PermissionApi.instance();

    public void sendPlayerListHeaderAndFooter(Player player) {
        permissionApi.user(player.getUniqueId()).flatMap(
                PermissionUser::highestPermissionGroup
        ).ifPresent(permissionGroup -> {
            Integer maxPlayers = syncProxyService.currentActiveConfiguration().map(
                    SyncProxyConfiguration::maxPlayers
            ).orElse(0);
            Optional<ServerInfo> serverInfo = player.getCurrentServer().map(ServerConnection::getServer).map(RegisteredServer::getServerInfo);
            List<Component> arguments = List.of(
                    text(playerService.onlinePlayerCount()),
                    serverInfo.map(info -> text(info.getName())).orElse(text("?")),
                    serverInfo.map(ServerInfo::getName).map(
                            s -> s.split("-")[0]
                    ).map(Component::text).orElse(text("?")),
                    permissionGroup.coloredName(),
                    text(maxPlayers)
            );

            player.sendPlayerListHeaderAndFooter(
                    translatable("nucleo.tabdecoration.header", arguments),
                    translatable("nucleo.tabdecoration.footer", arguments)
            );
        });
    }
}