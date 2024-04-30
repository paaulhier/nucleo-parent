package de.keeeks.nucleo.modules.translation.shared;

import de.keeeks.nucleo.modules.translations.api.ModuleDetails;
import de.keeeks.nucleo.modules.translations.api.TranslationEntry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Locale;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class DefaultTranslationEntry implements TranslationEntry {
    private final int id;
    private final String key;
    private final Locale locale;
    private final ModuleDetails module;
    private final Instant createdAt;

    private String value;
    private Instant updatedAt;

    public DefaultTranslationEntry(int id, String key, Locale locale, ModuleDetails module, String value) {
        this.id = id;
        this.key = key;
        this.locale = locale;
        this.module = module;
        this.value = value;

        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DefaultTranslationEntry that = (DefaultTranslationEntry) object;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}