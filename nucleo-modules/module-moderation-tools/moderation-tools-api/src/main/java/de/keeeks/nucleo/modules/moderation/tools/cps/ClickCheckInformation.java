package de.keeeks.nucleo.modules.moderation.tools.cps;

import java.time.Instant;
import java.util.UUID;

public record ClickCheckInformation(
        UUID viewer,
        UUID target,
        Instant startTimestamp
) {

    public ClickCheckInformation(UUID viewer, UUID target) {
        this(viewer, target, Instant.now());
    }
}