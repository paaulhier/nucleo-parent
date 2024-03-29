package de.keeeks.nucleo.modules.moderation.tools.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckApi;

public class ModerationToolsPlayerDisconnectListener {

    private final ClickCheckApi clickCheckApi = ServiceRegistry.service(ClickCheckApi.class);

    @Subscribe
    public void handleDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        clickCheckApi.removeClickCheckByTarget(player.getUniqueId());
        clickCheckApi.removeClickCheck(player.getUniqueId());
    }
}