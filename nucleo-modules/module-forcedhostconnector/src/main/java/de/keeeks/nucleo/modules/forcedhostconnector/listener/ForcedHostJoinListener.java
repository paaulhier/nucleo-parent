package de.keeeks.nucleo.modules.forcedhostconnector.listener;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.modules.forcedhostconnector.config.ForcedHost;
import de.keeeks.nucleo.modules.forcedhostconnector.config.ForcedHostConfiguration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ForcedHostJoinListener {
    private final ForcedHostConfiguration configuration;
    private final ProxyServer proxyServer;

    @Subscribe(order = PostOrder.LAST)
    public EventTask onPlayerJoin(PlayerChooseInitialServerEvent event) {
        Player player = event.getPlayer();
        return EventTask.async(() -> player.getVirtualHost().flatMap(
                inetSocketAddress -> configuration.forcedHost(inetSocketAddress.getHostName())
        ).filter(ForcedHost::enabled).filter(
                forcedHost -> forcedHost.requiredPermission() == null || player.hasPermission(forcedHost.requiredPermission())
        ).flatMap(forcedHost -> proxyServer.getServer(forcedHost.serverName())).ifPresent(event::setInitialServer));
    }
}