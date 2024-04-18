package de.keeeks.nucleo.modules.notifications.shared;

import de.keeeks.nucleo.modules.notifications.api.Notification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class NucleoNotification implements Notification {
    private final int id;
    private final String name;
    private final String description;
    private final String requiredPermission;
    private final Instant createdAt;
    private final Instant updatedAt;

    public NucleoNotification(int id, String name, String description) {
        this(id, name, description, null, Instant.now(), Instant.now());
    }

    public NucleoNotification(int id, String name, String description, String requiredPermission) {
        this(id, name, description, requiredPermission, Instant.now(), Instant.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NucleoNotification that = (NucleoNotification) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}