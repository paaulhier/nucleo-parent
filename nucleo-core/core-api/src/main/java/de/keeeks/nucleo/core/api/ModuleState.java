package de.keeeks.nucleo.core.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum ModuleState {
    ENABLED(false),
    FAILED_TO_ENABLE(true),
    DISABLED(false),
    FAILED_TO_DISABLE(true),
    INITIALIZED(false),
    FAILED_TO_INITIALIZE(true),
    UNAVAILABLE(false);

    private final boolean failed;

}