package de.keeeks.nucleo.modules.inventory;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.inventory.hotbar.PlayerHotBarService;
import de.keeeks.nucleo.modules.inventory.hotbar.listener.PlayerHotBarInteractListener;
import de.keeeks.nucleo.modules.inventory.hotbar.listener.PlayerHotBarInventoryClickEventListener;
import de.keeeks.nucleo.modules.inventory.hotbar.listener.PlayerHotBarItemDropListener;
import de.keeeks.nucleo.modules.inventory.items.PlaceholderItem;
import de.keeeks.nucleo.modules.inventory.items.page.PageBackItem;
import de.keeeks.nucleo.modules.inventory.items.page.PageForwardItem;
import xyz.xenondevs.invui.InvUI;
import xyz.xenondevs.invui.gui.structure.Structure;

@ModuleDescription(
        name = "inventory",
        version = "1.27",
        description = "https://github.com/NichtStudioCode/InvUI"
)
public class InventoryModule extends SpigotModule {

    @Override
    public void load() {
        ServiceRegistry.registerService(
                PlayerHotBarService.class,
                new PlayerHotBarService()
        );
    }

    @Override
    public void enable() {
        InvUI.getInstance().setPlugin(plugin());
        Structure.addGlobalIngredient(
                '#',
                new PlaceholderItem()
        );
        Structure.addGlobalIngredient(
                '>',
                new PageForwardItem()
        );
        Structure.addGlobalIngredient(
                '<',
                new PageBackItem()
        );
        registerListener(
                new PlayerHotBarInteractListener(),
                new PlayerHotBarInventoryClickEventListener(),
                new PlayerHotBarItemDropListener()
        );
    }
}