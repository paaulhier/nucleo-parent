package de.keeeks.nucleo.modules.translations.api;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface TranslationApi {
    String CHANNEL = "nucleo:translation";

    /**
     * Reloads the translations.
     */
    void reload();

    /**
     * Creates a new module. If a module with the same name already exists, nothing happens.
     *
     * @param name the name of the module
     */
    void createModule(String name);

    /**
     * Updates the module with the given id.
     *
     * @return the updated module
     */
    List<ModuleDetails> modules();

    /**
     * Deletes the module with the given id.
     *
     * @param name the name of the module
     * @return the deleted module
     */
    default Optional<ModuleDetails> module(String name) {
        return modules().stream().filter(
                moduleDetails -> moduleDetails.name().equals(name)
        ).findFirst();
    }

    /**
     * Returns the module with the given id.
     *
     * @param id the id of the module
     * @return the module
     */
    default Optional<ModuleDetails> module(int id) {
        return modules().stream().filter(
                moduleDetails -> moduleDetails.id() == id
        ).findFirst();
    }

    /**
     * Creates a new translation entry.
     *
     * @param moduleDetails the module details
     * @param locale        the locale of the translation
     * @param key           the key of the translation
     * @param value         the value of the translation
     * @return the created translation entry
     */
    TranslationEntry createTranslationEntry(
            ModuleDetails moduleDetails,
            Locale locale,
            String key,
            String value
    );

    /**
     * Creates a new translation entry with an empty value.
     *
     * @param moduleDetails the module details
     * @param locale        the locale of the translation
     * @param key           the key of the translation
     * @return the created translation entry
     */
    default TranslationEntry createTranslationEntry(
            ModuleDetails moduleDetails,
            Locale locale,
            String key
    ) {
        return createTranslationEntry(moduleDetails, locale, key, "");
    }

    /**
     * Updates the translation entry. If the translation entry does not exist, nothing happens.
     *
     * @param translationEntry the translation entry
     */
    void updateTranslationEntry(TranslationEntry translationEntry);

    /**
     * Deletes the translation entry. If the translation entry does not exist, nothing happens.
     *
     * @param translationEntry the translation entry
     */
    void deleteTranslationEntry(TranslationEntry translationEntry);

    /**
     * Returns all translation entries.
     *
     * @return all translation entries
     */
    List<TranslationEntry> translations();

    /**
     * Returns the translation entry with the given id.
     *
     * @param id the id of the translation entry
     * @return the translation entry
     */
    default Optional<TranslationEntry> translationEntry(int id) {
        return translations().stream().filter(
                translationEntry -> translationEntry.id() == id
        ).findFirst();
    }

    /**
     * Returns the translation entry with the given module, key and locale.
     *
     * @param moduleDetails the module details
     * @param key           the key
     * @param locale        the locale
     * @return the translation entry
     */
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

    /**
     * Returns all translation entries of the given module.
     *
     * @param module the module
     * @return all translation entries of the module
     */
    default List<TranslationEntry> translations(ModuleDetails module) {
        return translations().stream().filter(
                translationEntry -> translationEntry.module().equals(module)
        ).toList();
    }

    /**
     * Returns all translation entries of the given module.
     *
     * @param module the module
     * @return all translation entries of the module
     */
    default List<TranslationEntry> translations(String module) {
        return module(module).map(this::translations).orElse(List.of());
    }

    /**
     * Returns all translation entries of the given locale.
     *
     * @param locale the locale
     * @return all translation entries of the locale
     */
    default List<TranslationEntry> translations(Locale locale) {
        return translations().stream().filter(
                translationEntry -> translationEntry.locale().equals(locale)
        ).toList();
    }

    static Locale locale(String localeString) {
        String[] localeParts = localeString.split("-");

        if (localeParts.length == 1) {
            return Locale.of(localeParts[0]);
        } else if (localeParts.length == 2) {
            return Locale.of(localeParts[0], localeParts[1]);
        } else {
            return Locale.of(localeParts[0], localeParts[1], localeParts[2]);
        }
    }
}