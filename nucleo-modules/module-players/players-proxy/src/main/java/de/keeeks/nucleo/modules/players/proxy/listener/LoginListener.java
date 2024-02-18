package de.keeeks.nucleo.modules.players.proxy.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.proxy.NucleoProxyPlugin;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.md_5.bungee.protocol.Property;

public class LoginListener implements Listener {
    private final PlayerService playerService = ServiceRegistry.service(
            PlayerService.class
    );

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleLogin(LoginEvent event) {
        event.registerIntent(NucleoProxyPlugin.plugin());
        var connection = event.getConnection();
        var socketAddress = connection.getSocketAddress();
        Property property = event.getLoginResult().getProperties()[0];

        playerService.player(connection.getUniqueId()).ifPresentOrElse(
                nucleoPlayer -> {
                    handleSkinUpdate(
                            nucleoPlayer,
                            property
                    );
                    handleOnlinePlayerCreation(nucleoPlayer, socketAddress.toString());
                    event.completeIntent(NucleoProxyPlugin.plugin());
                },
                () -> {
                    NucleoPlayer nucleoPlayer = playerService.createPlayer(
                            connection.getUniqueId(),
                            connection.getName()
                    );
                    handleSkinUpdate(
                            nucleoPlayer,
                            property
                    );
                    handleOnlinePlayerCreation(nucleoPlayer, socketAddress.toString());
                    event.completeIntent(NucleoProxyPlugin.plugin());
                }
        );
    }

    private void handleSkinUpdate(NucleoPlayer onlinePlayer, Property property) {
        if (property.getName().equals("textures")) {
            onlinePlayer.updateSkin(
                    property.getValue(),
                    property.getSignature()
            );
        }
    }

    private void handleOnlinePlayerCreation(NucleoPlayer nucleoPlayer, String address) {
        NucleoOnlinePlayer onlinePlayer = playerService.createOnlinePlayer(
                nucleoPlayer,
                "",
                "",
                address.substring(1).split(":")[0]
        );
        onlinePlayer.properties().setProperty(
                "TestProperty",
                "TestValue"
        );
        onlinePlayer.updateLastLogin();
        playerService.updateNetworkWide(onlinePlayer);
    }

}