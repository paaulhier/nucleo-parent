package de.keeeks.nucleo.modules.inventory.hotbar.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.xenondevs.invui.item.ItemProvider;

@Getter
@RequiredArgsConstructor
public abstract class HotBarItem {

    public abstract ItemProvider itemProvider();

    public abstract void interact(
            Player player,
            int slot,
            PlayerInteractEvent event
    );
}