package de.keeeks.nucleo.modules.players.velocity.listener;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.velocity.PlayersVelocityModule;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

public class PlayerDisconnectListener {
    private final PlayerService playerService = ServiceRegistry.service(
            PlayerService.class
    );
    private final Logger logger = Module.module(PlayersVelocityModule.class).logger();

    @Subscribe(order = PostOrder.FIRST)
    public EventTask handleDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        return EventTask.async(() -> {
            logger.info("Player %s disconnected due to %s".formatted(
                    player.getUsername(),
                    event.getLoginStatus().name())
            );

            playerService.onlinePlayer(player.getUniqueId()).ifPresent(
                    nucleoOnlinePlayer -> playerService.savePlayerToDatabase(
                            nucleoOnlinePlayer.updateLastLogout().addOnlineTime(
                                    calculatePlayedTime(nucleoOnlinePlayer)
                            )
                    )
            );

            playerService.invalidateCacheNetworkWide(player.getUniqueId());
        });
    }

    private long calculatePlayedTime(NucleoOnlinePlayer nucleoOnlinePlayer) {
        return nucleoOnlinePlayer.lastLogin().until(
                Instant.now(),
                ChronoUnit.MILLIS
        );
    }
}