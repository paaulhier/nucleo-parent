package de.keeeks.nucleo.modules.translation.global;

import de.keeeks.nucleo.modules.translation.global.configuration.ConfigurationTranslationEntry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Locale;
import java.util.function.Function;

public record TranslationEntry(
        String key,
        String value,
        Locale locale
) {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public Component toComponent() {
        return toComponent(component -> component);
    }

    public Component toComponent(Function<Component, Component> componentModifier) {
        Component component = miniMessage.deserialize(
                value
        );

        return componentModifier.apply(component);
    }

    public static TranslationEntry of(String key, String value, Locale locale) {
        return new TranslationEntry(key, value, locale);
    }

    public static TranslationEntry of(String key, String value) {
        return new TranslationEntry(key, value, Locale.getDefault());
    }

    public static TranslationEntry of(ConfigurationTranslationEntry translationEntry, Locale locale) {
        return of(
                translationEntry.key(),
                translationEntry.value(),
                locale
        );
    }
}