package de.keeeks.nucleo.modules.automessage.shared;

import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class NucleoAutomaticMessage implements AutomaticMessage {

    private final int id;

    private final Instant createdAt;
    private final UUID createdBy;

    private Component message;
    private Instant updatedAt;
    private UUID lastUpdatedBy;
    private boolean enabled;

    public NucleoAutomaticMessage(int id, UUID createdBy, Component message) {
        this.id = id;
        this.createdAt = Instant.now();
        this.createdBy = createdBy;
        this.message = message;
    }

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public AutomaticMessage update(UUID updater) {
        this.lastUpdatedBy = updater;
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public AutomaticMessage message(Component message) {
        this.message = message;
        return this;
    }

    @Override
    public AutomaticMessage enabled(boolean state) {
        this.enabled = state;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NucleoAutomaticMessage that = (NucleoAutomaticMessage) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}