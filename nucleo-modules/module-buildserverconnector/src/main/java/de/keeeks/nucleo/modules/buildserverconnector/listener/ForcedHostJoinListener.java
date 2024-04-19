package de.keeeks.nucleo.modules.buildserverconnector.listener;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.modules.buildserverconnector.config.BuildServerConnectorConfiguration;
import lombok.RequiredArgsConstructor;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class ForcedHostJoinListener {
    private final BuildServerConnectorConfiguration configuration;
    private final ProxyServer proxyServer;

    @Subscribe(order = PostOrder.LAST)
    public EventTask onPlayerJoin(PlayerChooseInitialServerEvent event) {
        Player player = event.getPlayer();
        return EventTask.async(() -> {
            if (!player.hasPermission(configuration.requiredPermission())) {
                return;
            }

            player.getVirtualHost().filter(
                    inetSocketAddress -> inetSocketAddress.getHostName().equalsIgnoreCase(configuration.forcedHost())
            ).flatMap(inetSocketAddress -> proxyServer.getServer(
                    configuration.serverName()
            )).ifPresent(event::setInitialServer);
        });
    }
}