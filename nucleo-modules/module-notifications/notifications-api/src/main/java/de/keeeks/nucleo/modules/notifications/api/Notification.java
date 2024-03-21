package de.keeeks.nucleo.modules.notifications.api;

import java.time.Instant;
import java.util.UUID;

public interface Notification {

    int id();

    String name();

    String description();

    Instant createdAt();

    Instant updatedAt();

}