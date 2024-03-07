package de.keeeks.nucleo.syncproxy.api;

import net.kyori.adventure.text.Component;

import java.time.Instant;

public record MotdConfiguration(
        int id,
        int configurationId,
        Component firstLine,
        Component secondLine,
        Instant createdAt,
        Instant updatedAt
) {

    public Component fixedMotd() {
        return Component.textOfChildren(
                firstLine,
                Component.text("\n"),
                secondLine
        );
    }
}