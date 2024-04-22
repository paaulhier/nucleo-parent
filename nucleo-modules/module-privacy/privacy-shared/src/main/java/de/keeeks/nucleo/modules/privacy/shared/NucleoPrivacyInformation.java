package de.keeeks.nucleo.modules.privacy.shared;

import de.keeeks.nucleo.modules.privacy.api.PrivacyInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class NucleoPrivacyInformation implements PrivacyInformation {
    private final int id;
    private final UUID playerId;
    private final Instant createdAt;

    private boolean accepted;
    private Instant acceptedAt;

    public NucleoPrivacyInformation(int id, UUID playerId, Instant createdAt) {
        this.id = id;
        this.playerId = playerId;
        this.createdAt = createdAt;
        this.accepted = false;
        this.acceptedAt = null;
    }

    @Override
    public PrivacyInformation accept() {
        this.accepted = true;
        this.acceptedAt = Instant.now();
        return this;
    }
}