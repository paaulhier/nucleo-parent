package de.keeeks.nucleo.modules.inventory.hotbar.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.inventory.hotbar.PlayerHotBar;
import de.keeeks.nucleo.modules.inventory.hotbar.PlayerHotBarService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerHotBarInteractListener implements Listener {

    private final PlayerHotBarService playerHotBarService = ServiceRegistry.service(
            PlayerHotBarService.class
    );

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;

        PlayerHotBar playerHotBar = playerHotBarService.hotBar(player);
        if (playerHotBar == null) return;
        int heldItemSlot = player.getInventory().getHeldItemSlot();

        playerHotBar.item(heldItemSlot).ifPresent(hotBarItem -> {
            if (!hotBarItem.allowLeftClick() && event.getAction().isLeftClick()) return;
            hotBarItem.interact(player, heldItemSlot, event);
        });
    }

    @EventHandler
    public void handleInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        if (inventory.getItem(EquipmentSlot.HAND).getType().isAir()) return;

        PlayerHotBar playerHotBar = playerHotBarService.hotBar(player);
        if (playerHotBar == null) return;
        int heldItemSlot = inventory.getHeldItemSlot();

        playerHotBar.item(heldItemSlot).ifPresent(
                hotBarItem -> hotBarItem.interactAtEntity(player, heldItemSlot, event.getRightClicked())
        );
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handleDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            PlayerHotBar playerHotBar = playerHotBarService.hotBar(player);
            if (playerHotBar == null) return;
            int heldItemSlot = player.getInventory().getHeldItemSlot();

            playerHotBar.item(heldItemSlot).ifPresent(hotBarItem -> {
                if (!hotBarItem.allowLeftClick()) return;
                hotBarItem.interactAtEntity(player, heldItemSlot, event.getEntity());
            });
        }
    }
}