package de.keeeks.nucleo.modules.inventory.hotbar.item;

import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.xenondevs.invui.item.ItemProvider;

@Getter
public abstract class HotBarItem {

    private boolean allowLeftClick = false;

    public HotBarItem() {
    }

    public HotBarItem(boolean allowLeftClick) {
        this.allowLeftClick = allowLeftClick;
    }

    public abstract ItemProvider itemProvider();

    public abstract void interact(
            Player player,
            int slot,
            PlayerInteractEvent event
    );

    public void interactAtEntity(
            Player player,
            int slot,
            Entity entity
    ) {
    }
}