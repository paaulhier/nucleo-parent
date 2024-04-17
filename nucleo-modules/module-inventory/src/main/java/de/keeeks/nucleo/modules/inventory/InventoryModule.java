package de.keeeks.nucleo.modules.inventory;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.inventory.hotbar.PlayerHotBarService;
import de.keeeks.nucleo.modules.inventory.hotbar.listener.PlayerHotBarInteractListener;
import de.keeeks.nucleo.modules.inventory.hotbar.listener.PlayerHotBarInventoryClickEventListener;
import xyz.xenondevs.invui.InvUI;

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
        registerListener(
                new PlayerHotBarInteractListener(),
                new PlayerHotBarInventoryClickEventListener()
        );
    }
}