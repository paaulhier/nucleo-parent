package de.keeeks.nucleo.modules.inventory.hotbar.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.inventory.hotbar.PlayerHotBar;
import de.keeeks.nucleo.modules.inventory.hotbar.PlayerHotBarService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerHotBarInteractListener implements Listener {

    private final PlayerHotBarService playerHotBarService = ServiceRegistry.service(
            PlayerHotBarService.class
    );

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (!event.getAction().isRightClick() || item == null) return;

        PlayerHotBar playerHotBar = playerHotBarService.hotBar(player);
        if (playerHotBar == null) return;
        int heldItemSlot = player.getInventory().getHeldItemSlot();

        playerHotBar.item(heldItemSlot).ifPresent(
                hotBarItem -> hotBarItem.interact(player, heldItemSlot, event)
        );
    }
}