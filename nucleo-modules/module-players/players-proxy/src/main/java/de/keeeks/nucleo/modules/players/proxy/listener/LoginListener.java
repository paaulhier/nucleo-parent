package de.keeeks.nucleo.modules.players.proxy.listener;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.proxy.NucleoProxyPlugin;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.Version;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.md_5.bungee.protocol.Property;

public class LoginListener implements Listener {
    private final PlayerService playerService = ServiceRegistry.service(
            PlayerService.class
    );

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleLogin(LoginEvent event) {
        if (event.isCancelled()) return;

        event.registerIntent(NucleoProxyPlugin.plugin());
        var connection = event.getConnection();
        var socketAddress = connection.getSocketAddress();
        Property property = event.getLoginResult().getProperties()[0];

        Version version = Version.byProtocol(event.getConnection().getVersion());

        if (version == null) {
            event.setCancelled(true);
            event.setReason(TextComponent.fromLegacy(
                    "§cYour client version is not supported by this server. Please use %s".formatted(
                            Version.supportedVersionsAsString()
                    )
            ));
            event.completeIntent(NucleoProxyPlugin.plugin());
            return;
        }

        playerService.player(connection.getUniqueId()).ifPresentOrElse(
                nucleoPlayer -> {
                    handleSkinUpdate(
                            nucleoPlayer,
                            property
                    );
                    if (!nucleoPlayer.name().equals(connection.getName())) {
                        playerService.updatePlayerName(
                                nucleoPlayer.uuid(),
                                connection.getName()
                        );
                    }
                    handleOnlinePlayerCreation(nucleoPlayer.updateName(
                            connection.getName()
                    ), socketAddress.toString(), version);
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
                    handleOnlinePlayerCreation(nucleoPlayer, socketAddress.toString(), version);
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

    private void handleOnlinePlayerCreation(
            NucleoPlayer nucleoPlayer,
            String address,
            Version version
    ) {
        NucleoOnlinePlayer onlinePlayer = playerService.createOnlinePlayer(
                nucleoPlayer,
                "",
                Module.serviceName(),
                address.substring(1).split(":")[0],
                version
        );
        onlinePlayer.properties().setProperty(
                "TestProperty",
                "TestValue"
        );
        onlinePlayer.updateLastLogin();
        playerService.updateNetworkWide(onlinePlayer);
    }

}