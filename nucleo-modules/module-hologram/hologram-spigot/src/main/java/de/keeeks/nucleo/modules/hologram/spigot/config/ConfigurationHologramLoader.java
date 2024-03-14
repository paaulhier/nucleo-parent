package de.keeeks.nucleo.modules.hologram.spigot.config;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.hologram.api.Hologram;
import de.keeeks.nucleo.modules.hologram.api.HologramApi;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ConfigurationHologramLoader {
    private static final JsonConfiguration jsonConfiguration = JsonConfiguration.create(
            Module.module("holograms").dataFolder(),
            "holograms"
    );
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    private static final HologramApi hologramApi = ServiceRegistry.service(HologramApi.class);

    public static void loadFromConfiguration() {
        for (ConfigurationHologram configurationHologram : jsonConfiguration.loadObject(
                ConfigurationHologramList.class,
                ConfigurationHologramList.defaultConfiguration()
        ).holograms()) {
            Hologram hologram = hologramApi.createHologram(
                    configurationHologram.name(),
                    configurationHologram.location()
            );

            for (String line : configurationHologram.lines()) {
                if (line.startsWith("mat:")) {
                    Material material = Material.getMaterial(line.substring(4));
                    if (material != null) {
                        hologram.addLine(new ItemStack(material));
                    }
                } else {
                    Component component = miniMessage.deserialize(line);
                    hologram.addLine(component);
                }
            }
        }
    }
}