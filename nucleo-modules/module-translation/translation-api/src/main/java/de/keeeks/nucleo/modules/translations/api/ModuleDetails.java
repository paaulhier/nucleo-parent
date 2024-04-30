package de.keeeks.nucleo.modules.translations.api;

import java.time.Instant;

public interface ModuleDetails {
    int id();

    String name();

    Instant createdAt();

    Instant updatedAt();
}