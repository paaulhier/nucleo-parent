package de.keeeks.nucleo.modules.players.proxy.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class PlayerDisconnectListener implements Listener {
    private final PlayerService playerService = ServiceRegistry.service(
            PlayerService.class
    );

    @EventHandler
    public void handleDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        playerService.onlinePlayer(player.getUniqueId()).ifPresent(
                nucleoOnlinePlayer -> playerService.savePlayerToDatabase(nucleoOnlinePlayer.updateLastLogout().addOnlineTime(
                        nucleoOnlinePlayer.lastLogin().until(
                                Instant.now(),
                                ChronoUnit.MILLIS
                        )
                ))
        );

        playerService.invalidateCacheNetworkWide(player.getUniqueId());
    }
}