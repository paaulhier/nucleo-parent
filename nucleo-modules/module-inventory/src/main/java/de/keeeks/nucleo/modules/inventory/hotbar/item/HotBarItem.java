package de.keeeks.nucleo.modules.inventory.hotbar.item;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public abstract class HotBarItem extends AbstractItem {

    public abstract void interact(
            Player player,
            int slot,
            PlayerInteractEvent event
    );

    @Override
    public final void handleClick(
            @NotNull ClickType clickType,
            @NotNull Player player,
            @NotNull InventoryClickEvent inventoryClickEvent
    ) {
        throw new UnsupportedOperationException("HotBarItem does not support InventoryClickEvent");
    }
}