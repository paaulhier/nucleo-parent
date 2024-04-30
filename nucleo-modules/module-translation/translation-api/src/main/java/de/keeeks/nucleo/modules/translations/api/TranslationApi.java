package de.keeeks.nucleo.modules.translations.api;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface TranslationApi {
    String CHANNEL = "nucleo:translation";

    ModuleDetails createModule(String name);

    List<ModuleDetails> modules();

    default Optional<ModuleDetails> module(String name) {
        return modules().stream().filter(
                moduleDetails -> moduleDetails.name().equals(name)
        ).findFirst();
    }

    default Optional<ModuleDetails> module(int id) {
        return modules().stream().filter(
                moduleDetails -> moduleDetails.id() == id
        ).findFirst();
    }

    TranslationEntry createTranslationEntry(
            ModuleDetails moduleDetails,
            Locale locale,
            String key,
            String value
    );

    default TranslationEntry createTranslationEntry(
            ModuleDetails moduleDetails,
            Locale locale,
            String key
    ) {
        return createTranslationEntry(moduleDetails, locale, key, "");
    }

    List<TranslationEntry> translations();

    default Optional<TranslationEntry> translationEntry(
            ModuleDetails moduleDetails,
            String key,
            Locale locale
    ) {
        return translations().stream().filter(
                translationEntry -> translationEntry.module().equals(moduleDetails)
                        && translationEntry.key().equals(key)
                        && translationEntry.locale().equals(locale)
        ).findFirst();
    }

    default List<TranslationEntry> translations(ModuleDetails module) {
        return translations().stream().filter(
                translationEntry -> translationEntry.module().equals(module)
        ).toList();
    }

    default List<TranslationEntry> translations(String module) {
        return module(module).map(this::translations).orElse(List.of());
    }

    default List<TranslationEntry> translations(Locale locale) {
        return translations().stream().filter(
                translationEntry -> translationEntry.locale().equals(locale)
        ).toList();
    }
}