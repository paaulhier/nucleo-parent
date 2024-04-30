package de.keeeks.nucleo.modules.translations.api;

import java.time.Instant;
import java.util.Locale;

public interface TranslationEntry {
    int id();

    String key();

    String value();

    Locale locale();

    ModuleDetails module();

    Instant createdAt();

    Instant updatedAt();

    TranslationEntry value(String value);

}
