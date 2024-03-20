package de.keeeks.nucleo.modules.automessage.api;

import net.kyori.adventure.text.Component;

import java.time.Instant;
import java.util.UUID;

public interface AutomaticMessage {

    int id();

    Component message();

    AutomaticMessage message(Component message);

    AutomaticMessage enabled(boolean state);

    Instant createdAt();

    UUID createdBy();

    Instant updatedAt();

    UUID lastUpdatedBy();

    boolean enabled();

    AutomaticMessage update(UUID updater);
}