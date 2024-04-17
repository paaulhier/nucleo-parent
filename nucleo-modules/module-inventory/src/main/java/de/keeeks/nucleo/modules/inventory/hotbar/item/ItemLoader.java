package de.keeeks.nucleo.modules.inventory.hotbar.item;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.inventory.InventoryModule;
import org.bukkit.Bukkit;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;

import java.util.function.Consumer;

public final class ItemLoader {
    private final InventoryModule module = Module.module(InventoryModule.class);

    public void loadItemProvider(Item item, Consumer<ItemProvider> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(
                module.plugin(),
                () -> {
                    ItemProvider itemProvider;

                    while ((itemProvider = item.getItemProvider()) == null) {
                        //do nothing, just waiting til the item provider is loaded
                    }

                    consumer.accept(itemProvider);
                }
        );
    }
}