package de.keeeks.nucleo.modules.translation.global.configuration;

import java.util.List;

public record TranslationEntryConfiguration(
        List<ConfigurationTranslationEntry> translations
) {

    public static TranslationEntryConfiguration createDefault() {
        return new TranslationEntryConfiguration(List.of(
                ConfigurationTranslationEntry.defaultValue()
        ));
    }
}