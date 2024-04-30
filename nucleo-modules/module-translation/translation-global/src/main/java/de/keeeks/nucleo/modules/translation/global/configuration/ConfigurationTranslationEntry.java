package de.keeeks.nucleo.modules.translation.global.configuration;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@Deprecated
public record ConfigurationTranslationEntry(
        String key,
        String value
) {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static ConfigurationTranslationEntry of(String key, String value) {
        return new ConfigurationTranslationEntry(key, value);
    }

    public static ConfigurationTranslationEntry of(String key, Component value) {
        return new ConfigurationTranslationEntry(key, miniMessage.serialize(value));
    }

    public static ConfigurationTranslationEntry defaultValue() {
        return of(
                "default.missing",
                Component.translatable(
                        "default.missing"
                )
        );
    }

}