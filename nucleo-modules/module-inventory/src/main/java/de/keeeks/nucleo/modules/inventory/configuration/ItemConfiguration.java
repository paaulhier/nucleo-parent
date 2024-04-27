package de.keeeks.nucleo.modules.inventory.configuration;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.item.builder.AbstractItemBuilder;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;

import java.util.Arrays;
import java.util.List;

public record ItemConfiguration(
        Material material,
        String displayName,
        boolean translateDisplayName,
        LoreLine[] loreLines,
        String skinTexture
) {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public <T> AbstractItemBuilder<T> itemBuilder() {
        if (material == Material.PLAYER_HEAD) {
            return (AbstractItemBuilder<T>) new SkullBuilder(new SkullBuilder.HeadTexture(skinTexture))
                    .setDisplayName(new AdventureComponentWrapper(displayNameComponent()))
                    .addLoreLines(loreComponents().stream().map(
                            AdventureComponentWrapper::new
                    ).toArray(AdventureComponentWrapper[]::new));
        }
        return (AbstractItemBuilder<T>) new ItemBuilder(material)
                .setDisplayName(displayName)
                .addLoreLines(loreComponents().stream().map(
                        AdventureComponentWrapper::new
                ).toArray(AdventureComponentWrapper[]::new));
    }

    public Component displayNameComponent() {
        return translateDisplayName ? Component.translatable(displayName) : miniMessage.deserialize(displayName);
    }

    public List<Component> loreComponents() {
        return Arrays.stream(loreLines).map(LoreLine::component).toList();
    }

    public static ItemConfiguration load(Module module, String itemName) {
        return JsonConfiguration.create(
                module.dataFolder(),
                itemName
        ).loadObject(ItemConfiguration.class, defaultConfiguration());
    }

    private static ItemConfiguration defaultConfiguration() {
        return new ItemConfiguration(
                Material.STONE,
                "Item",
                false,
                new LoreLine[] {
                        new LoreLine("First line", false),
                        new LoreLine("Second line", false)
                },
                null
        );
    }
}