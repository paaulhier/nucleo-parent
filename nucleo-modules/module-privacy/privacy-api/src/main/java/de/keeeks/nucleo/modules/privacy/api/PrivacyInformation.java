package de.keeeks.nucleo.modules.privacy.api;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface PrivacyInformation {

    int id();

    UUID playerId();

    String playerName();

    boolean accepted();

    PrivacyInformation accept(
            String playerName,
            String ipAddress
    );

    Optional<String> ipAddress();

    Instant createdAt();

    Instant acceptedAt();

}