package de.keeeks.nucleo.modules.tabdecoration.service;

import de.keeeks.lejet.api.permission.PermissionApi;
import de.keeeks.lejet.api.permission.PermissionUser;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.tabdecoration.TabDecorationModule;
import de.keeeks.nucleo.syncproxy.api.SyncProxyConfiguration;
import de.keeeks.nucleo.syncproxy.api.SyncProxyService;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public final class TabDecorationService {
    private final SyncProxyService syncProxyService = ServiceRegistry.service(SyncProxyService.class);
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final PermissionApi permissionApi = PermissionApi.instance();
    private final String serviceName;

    public TabDecorationService() {
        var rawServiceName = TabDecorationModule.serviceName();

        if (rawServiceName.contains("-")) {
            this.serviceName = rawServiceName.substring(0, rawServiceName.indexOf("-"));
        } else {
            this.serviceName = rawServiceName;
        }
    }

    public void sendPlayerListHeaderAndFooter(Player player) {
        permissionApi.user(player.getUniqueId()).flatMap(
                PermissionUser::highestPermissionGroup
        ).ifPresent(permissionGroup -> {
            Integer maxPlayers = syncProxyService.currentActiveConfiguration().map(
                    SyncProxyConfiguration::maxPlayers
            ).orElse(0);
            List<Component> arguments = List.of(
                    text(playerService.onlinePlayerCount()),
                    text(serviceName),
                    permissionGroup.coloredName(),
                    text(PlaceholderAPI.setPlaceholders(player, permissionGroup.imageName())),
                    text(PlaceholderAPI.setPlaceholders(player, permissionGroup.chatImageName())),
                    text(maxPlayers)
            );

            player.sendPlayerListHeaderAndFooter(
                    translatable("nucleo.tabdecoration.header", arguments),
                    translatable("nucleo.tabdecoration.footer", arguments)
            );
        });
    }

    public void sendPlayerListHeaderAndFooterToAll() {
        Bukkit.getOnlinePlayers().forEach(this::sendPlayerListHeaderAndFooter);
    }
}