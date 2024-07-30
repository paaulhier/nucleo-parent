package de.keeeks.nucleo.modules.tabdecoration.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.tabdecoration.service.TabDecorationService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public final class TabDecorationLoginListener implements Listener {
    private final TabDecorationService tabDecorationService = ServiceRegistry.service(TabDecorationService.class);

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleLogin(PlayerJoinEvent event) {
        tabDecorationService.sendPlayerListHeaderAndFooterToAll();
    }
}