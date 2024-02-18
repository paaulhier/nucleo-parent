package de.keeeks.nucleo.modules.players.proxy.listener;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ServerConnectedListener implements Listener {

    private final PlayerService playerService = ServiceRegistry.service(
            PlayerService.class
    );

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleServerConnect(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();

        playerService.onlinePlayer(player.getUniqueId()).ifPresentOrElse(
                nucleoOnlinePlayer -> {
                    if (nucleoOnlinePlayer.locale() == null) {
                        nucleoOnlinePlayer.updateLocale(player.getLocale());
                        Module.module("players").logger().info(
                                "Updated locale for " + nucleoOnlinePlayer.name() + " to " + player.getLocale()
                        );
                    }

                    nucleoOnlinePlayer.updateServer(
                            event.getServer().getInfo().getName()
                    ).update();
                },
                () -> {
                    player.disconnect(TextComponent.fromLegacy(
                            "Sorry, but there was an error while trying to connect you to the server. Please try again.",
                            ChatColor.RED
                    ));
                    throw new IllegalStateException("Player is not online");
                }
        );

    }

}