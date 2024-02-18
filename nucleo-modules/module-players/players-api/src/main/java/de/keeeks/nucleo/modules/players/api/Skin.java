package de.keeeks.nucleo.modules.players.api;

import java.util.UUID;

public record Skin(
        UUID playerId,
        String value,
        String signature
) {
}