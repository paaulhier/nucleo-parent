package de.keeeks.nucleo.modules.privacy.spigot.inventory;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.inventory.configuration.InventoryConfiguration;
import de.keeeks.nucleo.modules.inventory.configuration.ItemConfiguration;
import de.keeeks.nucleo.modules.privacy.api.PrivacyApi;
import de.keeeks.nucleo.modules.privacy.spigot.PrivacySpigotModule;
import de.keeeks.nucleo.modules.privacy.spigot.inventory.item.AcceptPrivacyItem;
import de.keeeks.nucleo.modules.privacy.spigot.inventory.item.DeclinePrivacyItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public class PrivacyInventory {
    private final PrivacyApi privacyApi = ServiceRegistry.service(PrivacyApi.class);
    private final InventoryConfiguration inventoryConfiguration = InventoryConfiguration.load(
            Module.module(PrivacySpigotModule.class),
            PrivacyInventory.class
    );

    private final Gui gui;

    public PrivacyInventory() {
        ItemConfiguration informationItemConfiguration = ItemConfiguration.load(
                Module.module(PrivacySpigotModule.class),
                "informationItem"
        );
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
                informationItemConfiguration.itemBuilder()
        ));
    }

    public void open(Player player) {
        Window.single()
                .setGui(gui)
                .setTitle(new AdventureComponentWrapper(inventoryConfiguration.titleComponent()))
                .addCloseHandler(() -> privacyApi.privacyInformation(player.getUniqueId()).ifPresent(
                        privacyInformation -> {
                            if (!privacyInformation.accepted()) {
                                Bukkit.getScheduler().runTaskLater(
                                        Module.module(PrivacySpigotModule.class).plugin(),
                                        () -> open(player),
                                        2L
                                );
                            }
                        }
                ))
                .build(player)
                .open();
    }
}