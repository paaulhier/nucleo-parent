package de.keeeks.nucleo.modules.inventory.items.page;

import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class PageBackItem extends InventoryPageItem {
    public PageBackItem() {
        super(true);
    }


    @Override
    public List<AdventureComponentWrapper> buildItemLore(boolean forward, boolean hasAnotherPage, int newPage) {
        if (hasAnotherPage) {
            return List.of(new AdventureComponentWrapper(
                    translatable(
                            "invui.controlItems.pageBackLore",
                            text(newPage)
                    )
            ));
        } else {
            return List.of(new AdventureComponentWrapper(
                    translatable("invui.controlItems.pageBackLoreEnd")
            ));
        }
    }
}