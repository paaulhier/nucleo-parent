package de.keeeks.nucleo.modules.syncproxy.velocity.listener;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.syncproxy.api.SyncProxyService;

public class ProxyPingListener {
    private final SyncProxyService syncProxyService = ServiceRegistry.service(
            SyncProxyService.class
    );
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);

    @Subscribe(order = PostOrder.FIRST)
    public EventTask handlePing(ProxyPingEvent event) {
        return EventTask.async(() -> {
            ServerPing.Builder pingBuilder = ServerPing.builder();

            syncProxyService.currentActiveConfiguration().ifPresent(syncProxyConfiguration -> {
                pingBuilder.clearSamplePlayers()
                        .maximumPlayers(syncProxyConfiguration.maxPlayers())
                        .onlinePlayers(playerService.onlinePlayerCount());
                if (syncProxyConfiguration.hasProtocol()) {
                    pingBuilder.version(new ServerPing.Version(
                            -1,
                            syncProxyConfiguration.protocolText()
                    ));
                    System.out.println("Setting version to -1 with text " + syncProxyConfiguration.protocolText());
                }

                syncProxyService.activeMotdConfiguration().ifPresent(
                        motdConfiguration -> pingBuilder.description(motdConfiguration.fixedMotd())
                );
            });

            event.setPing(pingBuilder.build());
        });
    }

}