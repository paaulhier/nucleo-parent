package de.keeeks.nucleo.modules.moderation.tools.spigot.cps;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ClicksPerSecondInformation(
        UUID uuid,
        List<ClickDetails> clicks
) {

    public int clicksPerSecondByType(ClickType clickType) {
        clicks.removeIf(clickDetails -> clickDetails.time().isBefore(Instant.now().minusSeconds(1)));
        return (int) clicks.stream().filter(clickDetails -> clickDetails.clickType() == clickType).count();
    }

    public record ClickDetails(
            ClickType clickType,
            Instant time
    ) {
    }

    public enum ClickType {
        LEFT,
        RIGHT;
    }

}