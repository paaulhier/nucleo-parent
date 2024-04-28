package de.keeeks.nucleo.modules.tabdecoration.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.tabdecoration.service.TabDecorationService;

public class TabDecorationLoginListener {
    private final TabDecorationService tabDecorationService = ServiceRegistry.service(TabDecorationService.class);

    @Subscribe
    public void handleLogin(PostLoginEvent event) {
        Player player = event.getPlayer();
        tabDecorationService.sendPlayerListHeaderAndFooter(player);
    }
}