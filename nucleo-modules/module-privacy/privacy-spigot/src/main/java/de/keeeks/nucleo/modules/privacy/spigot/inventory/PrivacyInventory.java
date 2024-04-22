package de.keeeks.nucleo.modules.privacy.spigot.inventory;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.privacy.api.PrivacyApi;
import de.keeeks.nucleo.modules.privacy.spigot.inventory.item.AcceptPrivacyItem;
import de.keeeks.nucleo.modules.privacy.spigot.inventory.item.DeclinePrivacyItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.window.Window;

public class PrivacyInventory {
    private final PrivacyApi privacyApi = ServiceRegistry.service(PrivacyApi.class);

    private final Gui gui;

    public PrivacyInventory() {
        gui = Gui.of(new Structure(
                "XXXXXXXXX",
                "XXYXIXNXX",
                "XXXXXXXXX"
        ).addIngredient(
                'X',
                new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("")
        ).addIngredient(
                'Y',
                new AcceptPrivacyItem()
        ).addIngredient(
                'N',
                new DeclinePrivacyItem()
        ).addIngredient(
                'I',
                new SkullBuilder(new SkullBuilder.HeadTexture(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR" +
                                "1cmUvMzMyNzAwOWNkNDcxODg0MTE1MjAzODYwYzFkYWY2ZDgyNWMwODQ1ZjhhNTI4YzZlZjZiZDI0ZWI4NmI0Yj" +
                                "M4YiJ9fX0="
                )).setDisplayName(new AdventureComponentWrapper(Component.translatable(
                        "inventory.privacy.information"
                ))).addLoreLines(new AdventureComponentWrapper(Component.translatable(
                        "inventory.privacy.information.lore"
                )))
        ));
    }

    public void open(Player player) {
        Window.single()
                .setGui(gui)
                .setTitle(new AdventureComponentWrapper(Component.translatable(
                        "inventory.privacy.title"
                )))
                .addCloseHandler(() -> privacyApi.privacyInformation(player.getUniqueId()).ifPresent(
                        privacyInformation -> {
                            if (!privacyInformation.accepted()) open(player);
                        }
                ))
                .setCloseable(false)
                .build(player)
                .open();
    }
}