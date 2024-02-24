package de.keeeks.nucleo.core.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum ModuleState {
    LOADED(false),
    FAILED_TO_LOAD(true),
    ENABLED(false),
    FAILED_TO_ENABLE(true),
    DISABLED(false),
    FAILED_TO_DISABLE(true),
    INITIALIZED(false),
    FAILED_TO_INITIALIZE(true),
    UNAVAILABLE(false);

    private final boolean failed;

    public String display() {
        String lowerCaseName = name().toLowerCase().replaceAll("_", " ");
        return lowerCaseName.substring(0, 1).toUpperCase() + lowerCaseName.substring(1);
    }
}