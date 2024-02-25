package de.keeeks.nucleo.modules.syncproxy.proxy.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.proxy.NucleoProxyPlugin;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.syncproxy.api.configuration.SyncProxyService;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

@RequiredArgsConstructor
public class ProxyPingListener implements Listener {
    private final SyncProxyService syncProxyService = ServiceRegistry.service(
            SyncProxyService.class
    );
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final NucleoProxyPlugin plugin = NucleoProxyPlugin.plugin();

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleProxyPing(ProxyPingEvent event) {
        event.registerIntent(plugin);

        var response = event.getResponse();

        syncProxyService.currentActiveConfiguration().ifPresent(syncProxyConfiguration -> {
            response.setPlayers(new ServerPing.Players(
                    syncProxyConfiguration.maxPlayers(),
                    playerService.onlinePlayerCount(),
                    null
            ));
            if (syncProxyConfiguration.protocolText() != null) {
                response.setVersion(new ServerPing.Protocol(
                        syncProxyConfiguration.protocolText(),
                        -1
                ));
            }

            syncProxyService.activeMotdConfiguration().map(
                    motdConfiguration -> BungeeComponentSerializer.get().serialize(
                            motdConfiguration.fixedMotd()
                    )[0]
            ).ifPresent(response::setDescriptionComponent);
        });

        event.setResponse(response);
        event.completeIntent(plugin);
    }
}