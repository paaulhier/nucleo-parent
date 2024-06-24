package de.keeeks.nucleo.modules.inventory.hotbar.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.inventory.hotbar.PlayerHotBar;
import de.keeeks.nucleo.modules.inventory.hotbar.PlayerHotBarService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class PlayerHotBarInventoryClickEventListener implements Listener {
    private final PlayerHotBarService playerHotBarService = ServiceRegistry.service(
            PlayerHotBarService.class
    );

    @EventHandler
    public void handleInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            Inventory clickedInventory = event.getClickedInventory();
            if (clickedInventory == null || !clickedInventory.equals(player.getInventory())) return;
            if (event.getSlotType() != InventoryType.SlotType.QUICKBAR) return;
            PlayerHotBar playerHotBar = playerHotBarService.hotBar(player);
            if (playerHotBar == null) return;

            int hotBarButton = event.getHotbarButton();
            int slot = event.getSlot();

            playerHotBar.item(hotBarButton).ifPresent(hotBarItem -> event.setCancelled(true));
            playerHotBar.item(slot).ifPresent(hotBarItem -> event.setCancelled(true));
        }
    }
}