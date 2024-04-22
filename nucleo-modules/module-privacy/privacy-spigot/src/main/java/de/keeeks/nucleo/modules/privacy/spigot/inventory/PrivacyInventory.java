package de.keeeks.nucleo.modules.privacy.spigot.inventory;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.inventory.hotbar.component.AdventureComponentWrapper;
import de.keeeks.nucleo.modules.privacy.api.PrivacyApi;
import de.keeeks.nucleo.modules.privacy.spigot.inventory.item.AcceptPrivacyItem;
import de.keeeks.nucleo.modules.privacy.spigot.inventory.item.DeclinePrivacyItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public class PrivacyInventory {
    private final PrivacyApi privacyApi = ServiceRegistry.service(PrivacyApi.class);

    private final Gui gui;

    public PrivacyInventory() {
        gui = Gui.of(new Structure(
                "XXXXXXXXX",
                "XOYOOONOX",
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
                .build(player)
                .open();
    }
}