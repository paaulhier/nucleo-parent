package de.keeeks.nucleo.modules.notifications.api;

import java.time.Instant;

public interface Notification {

    int id();

    String name();

    String description();

    String requiredPermission();

    Instant createdAt();

    Instant updatedAt();

}