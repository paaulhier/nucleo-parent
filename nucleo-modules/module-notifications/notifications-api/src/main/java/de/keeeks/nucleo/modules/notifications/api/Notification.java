package de.keeeks.nucleo.modules.notifications.api;

import java.time.Instant;

public interface Notification {

    int id();

    String name();

    String description();

    Instant createdAt();

    Instant updatedAt();

}