package de.keeeks.nucleo.modules.privacy.spigot.inventory.item;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.privacy.api.PrivacyApi;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AsyncItem;

import static net.kyori.adventure.text.Component.translatable;

public class AcceptPrivacyItem extends AsyncItem {
    private static final PrivacyApi privacyApi = ServiceRegistry.service(PrivacyApi.class);

    public AcceptPrivacyItem() {
        super(new ItemBuilder(Material.CLOCK).setDisplayName(new AdventureComponentWrapper(
                translatable("inventory.privacy.loading")
        )), () -> new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(
                new de.keeeks.nucleo.modules.inventory.hotbar.component.AdventureComponentWrapper(
                        translatable("inventory.privacy.accept")
                )
        ));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        privacyApi.privacyInformation(player.getUniqueId()).ifPresent(privacyInformation -> {
            privacyApi.accept(privacyInformation);
            player.closeInventory();
        });
    }
}