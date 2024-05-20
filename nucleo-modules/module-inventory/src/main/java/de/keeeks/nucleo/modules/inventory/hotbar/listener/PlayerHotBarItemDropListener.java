package de.keeeks.nucleo.modules.inventory.hotbar.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.inventory.hotbar.PlayerHotBarService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerHotBarItemDropListener implements Listener {
    private final PlayerHotBarService playerHotBarService = ServiceRegistry.service(
            PlayerHotBarService.class
    );

    @EventHandler
    public void handleDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        playerHotBarService.hotBar(player).item(player.getInventory().getHeldItemSlot()).ifPresent(
                hotBarItem -> event.setCancelled(true)
        );
    }
}