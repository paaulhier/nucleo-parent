package de.keeeks.nucleo.modules.inventory.configuration;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import net.kyori.adventure.text.Component;

public record InventoryConfiguration(
        String title,
        boolean translateTitle
) {
    public Component titleComponent() {
        return translateTitle ? Component.translatable(title) : Component.text(title);
    }

    public static InventoryConfiguration load(Module module, Class<?> inventoryClass) {
        return JsonConfiguration.create(
                module.dataFolder(),
                inventoryClass.getSimpleName()
        ).loadObject(InventoryConfiguration.class, defaultConfiguration());
    }

    private static InventoryConfiguration defaultConfiguration() {
        return new InventoryConfiguration("Inventory", true);
    }
}