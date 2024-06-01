package de.keeeks.nucleo.modules.inventory.items.page;

import org.bukkit.Material;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

abstract class InventoryPageItem extends PageItem {
    protected final boolean forward;

    public InventoryPageItem(boolean forward) {
        super(forward);
        this.forward = forward;
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> pagedGui) {
        int newPage = forward ? pagedGui.getCurrentPage() + 1 : pagedGui.getCurrentPage() - 1;
        boolean hasAnotherPage = forward ? newPage < pagedGui.getPageAmount() : newPage > 0;

        ItemBuilder itemBuilder = new ItemBuilder(Material.ARROW);
        itemBuilder.setDisplayName(new AdventureComponentWrapper(
                translatable("invui.controlItems.page" + (forward ? "Forward" : "Back"))
        ));

        itemBuilder.addLoreLines(buildItemLore(forward, hasAnotherPage, newPage).toArray(new AdventureComponentWrapper[0]));

        return itemBuilder;
    }

    public abstract List<AdventureComponentWrapper> buildItemLore(
            boolean forward,
            boolean hasAnotherPage,
            int newPage
    );
}