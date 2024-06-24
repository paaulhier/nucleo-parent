package de.keeeks.nucleo.modules.translations.api;

import java.time.Instant;
import java.util.Locale;

public interface TranslationEntry {

    /**
     * Returns the id of the translation entry.
     *
     * @return the id of the translation entry.
     */
    int id();

    /**
     * Returns the key of the translation entry.
     *
     * @return the key of the translation entry.
     */
    String key();

    /**
     * Returns the value of the translation entry.
     *
     * @return the value of the translation entry.
     */
    String value();

    /**
     * Returns the locale of the translation entry.
     *
     * @return the locale of the translation entry.
     */
    Locale locale();

    /**
     * Returns the module details of the translation entry.
     *
     * @return the module details of the translation entry.
     */
    ModuleDetails module();

    /**
     * Returns the creation date of the translation entry.
     *
     * @return the creation date of the translation entry.
     */
    Instant createdAt();

    /**
     * Returns the last update date of the translation entry.
     *
     * @return the last update date of the translation entry.
     */
    Instant updatedAt();

    /**
     * Updates the key of this translation entry.
     *
     * @param key the new key.
     * @return this translation entry.
     */
    TranslationEntry key(String key);

    /**
     * Updates the value of this translation entry.
     *
     * @param value the new value.
     * @return this translation entry.
     */
    TranslationEntry value(String value);

    /**
     * Updates the locale of this translation entry.
     *
     * @param locale the new locale.
     * @return this translation entry.
     */
    TranslationEntry locale(Locale locale);
}
