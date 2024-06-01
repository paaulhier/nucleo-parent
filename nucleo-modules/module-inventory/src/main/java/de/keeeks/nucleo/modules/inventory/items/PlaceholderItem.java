package de.keeeks.nucleo.modules.inventory.items;

import org.bukkit.Material;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import static net.kyori.adventure.text.Component.text;

public class PlaceholderItem extends SimpleItem {
    public PlaceholderItem() {
        super(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(new AdventureComponentWrapper(
                text("")
        )));
    }
}