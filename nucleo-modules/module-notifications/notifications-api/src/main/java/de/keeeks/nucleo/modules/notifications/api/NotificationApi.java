package de.keeeks.nucleo.modules.notifications.api;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationApi {
    String CHANNEL = "nucleo:notifications";


    Notification createNotification(
            String name,
            String description,
            String requiredPermission
    );

    default Notification createNotification(
            String name,
            String description
    ) {
        return createNotification(name, description, null);
    }

    List<Notification> notifications();

    default Optional<Notification> notification(int id) {
        return notifications().stream()
                .filter(n -> n.id() == id)
                .findFirst();
    }

    default Optional<Notification> notification(String name) {
        return notifications().stream()
                .filter(n -> n.name().equals(name))
                .findFirst();
    }

    void deleteNotification(Notification notification);

    default void deleteNotification(int id) {
        notification(id).ifPresent(this::deleteNotification);
    }

    default void deleteNotification(String name) {
        notification(name).ifPresent(this::deleteNotification);
    }

    boolean notificationActive(Notification notification, UUID uuid);

    void notificationActive(Notification notification, UUID uuid, boolean active);
}