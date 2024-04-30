package de.keeeks.nucleo.modules.translation.shared;

import de.keeeks.nucleo.modules.translations.api.ModuleDetails;

import java.time.Instant;
import java.util.Objects;

public record DefaultModuleDetails(
        int id,
        String name,
        Instant createdAt,
        Instant updatedAt
) implements ModuleDetails {

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DefaultModuleDetails that = (DefaultModuleDetails) object;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}