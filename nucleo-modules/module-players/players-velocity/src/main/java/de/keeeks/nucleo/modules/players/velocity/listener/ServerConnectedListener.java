package de.keeeks.nucleo.modules.players.velocity.listener;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.shared.packet.player.NucleoOnlinePlayerSwitchServerPacket;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;

import static net.kyori.adventure.text.Component.text;

public class ServerConnectedListener {

    private final PlayerService playerService = ServiceRegistry.service(
            PlayerService.class
    );
    private final NatsConnection natsConnection = ServiceRegistry.service(
            NatsConnection.class
    );

    @Subscribe(order = PostOrder.FIRST)
    public EventTask handleServerSwitch(ServerPostConnectEvent event) {
        Player player = event.getPlayer();

        return EventTask.async(() -> {
            playerService.onlinePlayer(player.getUniqueId()).ifPresentOrElse(
                    nucleoOnlinePlayer -> player.getCurrentServer().map(
                            serverConnection -> serverConnection.getServerInfo().getName()
                    ).ifPresentOrElse(serverName -> {
                        nucleoOnlinePlayer.updateServer(serverName);
                        natsConnection.publishPacket(
                                PlayerService.CHANNEL,
                                new NucleoOnlinePlayerSwitchServerPacket(nucleoOnlinePlayer)
                        );
                    }, () -> disconnectDueToConnectError(player)),
                    () -> disconnectDueToConnectError(player)
            );
        });
    }

    private void disconnectDueToConnectError(Player player) {
        player.disconnect(text(
                "Sorry, but there was an error while trying to connect you to the server. Please try again.",
                Style.style(NamedTextColor.RED)
        ));
    }
}