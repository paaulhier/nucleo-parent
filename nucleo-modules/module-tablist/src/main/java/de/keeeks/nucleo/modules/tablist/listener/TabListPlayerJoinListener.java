package de.keeeks.nucleo.modules.tablist.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.tablist.service.TabListService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class TabListPlayerJoinListener implements Listener {
    private final TabListService tabListService = ServiceRegistry.service(TabListService.class);

    @EventHandler
    public void handleLogin(PlayerJoinEvent event) {
        tabListService.updateTabList();
    }
}