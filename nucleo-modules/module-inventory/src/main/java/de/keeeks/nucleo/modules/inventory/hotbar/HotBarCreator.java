package de.keeeks.nucleo.modules.inventory.hotbar;

import org.bukkit.entity.Player;

import java.util.UUID;

@FunctionalInterface
public interface HotBarCreator {
    PlayerHotBar create(UUID uuid);

    default PlayerHotBar create(Player player) {
        return create(player.getUniqueId());
    }
}