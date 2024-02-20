package de.keeeks.nucleo.modules.translation.global;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.function.BiFunction;

public class ComponentTranslator implements Translator {
    private final BiFunction<String, Locale, Component> componentAdapter;

    public ComponentTranslator(BiFunction<String, Locale, Component> componentAdapter) {
        this.componentAdapter = componentAdapter;
    }

    @Override
    public @NotNull Key name() {
        return Key.key(
                "i18n",
                "main"
        );
    }

    @Override
    public @Nullable MessageFormat translate(@NotNull String key, @NotNull Locale locale) {
        return null;
    }

    @Override
    public @Nullable Component translate(@NotNull TranslatableComponent component, @NotNull Locale locale) {
        return componentAdapter.apply(
                component.key(),
                locale
        );
    }
}