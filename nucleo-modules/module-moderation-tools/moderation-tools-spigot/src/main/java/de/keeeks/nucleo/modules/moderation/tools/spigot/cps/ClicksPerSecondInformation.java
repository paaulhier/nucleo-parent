package de.keeeks.nucleo.modules.moderation.tools.spigot.cps;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ClicksPerSecondInformation(
        UUID uuid,
        List<Instant> clicks
) {

    public int clicksPerSecond() {
        clicks.removeIf(instant -> instant.isBefore(Instant.now().minusSeconds(1)));
        return clicks.size();
    }
}