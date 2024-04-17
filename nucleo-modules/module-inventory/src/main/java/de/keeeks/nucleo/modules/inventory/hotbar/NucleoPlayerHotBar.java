package de.keeeks.nucleo.modules.inventory.hotbar;

import de.keeeks.nucleo.modules.inventory.hotbar.item.HotBarItem;
import de.keeeks.nucleo.modules.inventory.hotbar.item.ItemLoader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class NucleoPlayerHotBar implements PlayerHotBar {
    private static final ItemLoader itemLoader = new ItemLoader();

    private final Map<Integer, Supplier<HotBarItem>> hotbarItems = new HashMap<>();

    private final UUID id = UUID.randomUUID();
    private final UUID owner;

    @Override
    public <T extends HotBarItem> Optional<T> item(int slot) {
        return Optional.ofNullable(hotbarItems.get(slot)).map(
                t -> (T) ((Supplier<?>) t).get()
        );
    }

    @Override
    public void item(int slot, HotBarItem itemProvider) {
        item(slot, () -> itemProvider);
    }

    @Override
    public void item(int slot, Supplier<HotBarItem> itemProviderSupplier) {
        if (slot < 0 || slot > 8) {
            throw new IllegalArgumentException("Slot must be between 0 and 8");
        }
        hotbarItems.put(slot, itemProviderSupplier);
        update();
    }

    @Override
    public void clear() {
        for (Integer slot : Map.copyOf(hotbarItems).keySet()) {
            ownerInventory().ifPresent(inventory -> inventory.setItem(
                    slot,
                    null
            ));
            hotbarItems.remove(slot);
        }
    }

    @Override
    public void update() {
        for (Integer slot : Map.copyOf(hotbarItems).keySet()) {
            Supplier<HotBarItem> itemProviderSupplier = hotbarItems.get(slot);

            itemLoader.loadItemProvider(
                    itemProviderSupplier.get(),
                    itemProvider -> ownerInventory().ifPresent(inventory -> inventory.setItem(
                            slot,
                            itemProvider.get()
                    ))
            );
        }
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        NucleoPlayerHotBar that = (NucleoPlayerHotBar) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(id);
    }

    private Optional<Inventory> ownerInventory() {
        return Optional.ofNullable(Bukkit.getPlayer(owner)).map(
                HumanEntity::getInventory
        );
    }
}