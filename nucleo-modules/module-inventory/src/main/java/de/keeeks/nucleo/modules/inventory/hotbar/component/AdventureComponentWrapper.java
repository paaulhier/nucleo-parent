package de.keeeks.nucleo.modules.inventory.hotbar.component;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.inventoryaccess.component.ComponentWrapper;
import xyz.xenondevs.inventoryaccess.util.AdventureComponentUtils;

import java.util.Locale;

public record AdventureComponentWrapper(Component component) implements ComponentWrapper {
    private static final GsonComponentSerializer gsonComponentSerializer = GsonComponentSerializer.gson();

    @Override
    public @NotNull String serializeToJson() {
        return gsonComponentSerializer.serialize(component);
    }

    @Override
    public @NotNull ComponentWrapper localized(@NotNull String lang) {
        String[] localeParts = lang.split("_");
        Locale locale;

        if (localeParts.length == 1) {
            locale = Locale.of(localeParts[0]);
        } else if (localeParts.length == 2) {
            locale = Locale.of(localeParts[0], localeParts[1]);
        } else {
            locale = Locale.of(localeParts[0], localeParts[1], localeParts[2]);
        }

        return new xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper(GlobalTranslator.render(
                component,
                locale
        ));
    }

    @Override
    public @NotNull ComponentWrapper withoutPreFormatting() {
        return new xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper(AdventureComponentUtils.withoutPreFormatting(component));
    }

    @Override
    public @NotNull ComponentWrapper clone() {
        try {
            return (xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}