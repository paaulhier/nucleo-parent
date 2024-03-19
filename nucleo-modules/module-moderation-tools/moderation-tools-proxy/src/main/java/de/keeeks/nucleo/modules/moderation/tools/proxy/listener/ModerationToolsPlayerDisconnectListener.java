package de.keeeks.nucleo.modules.moderation.tools.proxy.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckApi;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ModerationToolsPlayerDisconnectListener implements Listener {

    private final ClickCheckApi clickCheckApi = ServiceRegistry.service(ClickCheckApi.class);

    @EventHandler
    public void handleDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        clickCheckApi.removeClickCheckByTarget(player.getUniqueId());
        clickCheckApi.removeClickCheck(player.getUniqueId());
    }
}