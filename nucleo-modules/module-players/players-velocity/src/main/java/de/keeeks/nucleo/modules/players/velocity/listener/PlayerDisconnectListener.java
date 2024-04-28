package de.keeeks.nucleo.modules.players.velocity.listener;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.utils.Formatter;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerDisconnectPacket;

import java.time.Duration;

public class PlayerDisconnectListener {
    private final PlayerService playerService = ServiceRegistry.service(
            PlayerService.class
    );
    private final NatsConnection natsConnection = ServiceRegistry.service(NatsConnection.class);

    @Subscribe(order = PostOrder.FIRST)
    public EventTask handleDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        return EventTask.async(() -> {
            playerService.onlinePlayer(player.getUniqueId()).ifPresent(
                    nucleoOnlinePlayer -> {
                        natsConnection.publishPacket(
                                PlayerService.CHANNEL,
                                new NucleoOnlinePlayerDisconnectPacket(nucleoOnlinePlayer)
                        );
                        playerService.savePlayerToDatabase(nucleoOnlinePlayer.updateLastLogout());
                    }
            );

            playerService.invalidateCacheNetworkWide(player.getUniqueId());
        });
    }
}