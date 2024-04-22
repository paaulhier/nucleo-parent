package de.keeeks.nucleo.modules.privacy.api;

import java.time.Instant;
import java.util.UUID;

public interface PrivacyInformation {

    int id();

    UUID playerId();

    boolean accepted();

    PrivacyInformation accept();

    Instant createdAt();

    Instant acceptedAt();

}