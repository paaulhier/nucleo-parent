package de.keeeks.nucleo.modules.inventory.hotbar;

import de.keeeks.nucleo.modules.inventory.hotbar.item.HotBarItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public interface PlayerHotBar {

    UUID id();

    UUID owner();

    default Optional<Player> ownerAsPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(owner()));
    }

    <T extends HotBarItem> Optional<T> item(int slot);

    void item(int slot, HotBarItem item);

    void item(int slot, Supplier<HotBarItem> itemSupplier);

    void clear();

    void update();

}