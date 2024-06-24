package de.keeeks.nucleo.modules.notifications.api;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationApi {
    String CHANNEL = "nucleo:notifications";

    /**
     * Creates a new notification. The required permission is optional.
     *
     * @param name               the name of the notification
     * @param description        the description of the notification
     * @param requiredPermission the required permission to see the notification
     * @return the created notification
     */
    Notification createNotification(
            String name,
            String description,
            String requiredPermission
    );

    /**
     * Creates a new notification without a required permission.
     *
     * @param name        the name of the notification
     * @param description the description of the notification
     * @return the created notification
     */
    default Notification createNotification(
            String name,
            String description
    ) {
        return createNotification(name, description, null);
    }

    /**
     * Returns all notifications.
     *
     * @return a list of all notifications
     */
    List<Notification> notifications();

    /**
     * Returns a notification by its id. If the notification does not exist, an empty optional is returned.
     *
     * @param id the id of the notification
     * @return the notification with the given id or an empty optional
     */
    default Optional<Notification> notification(int id) {
        return notifications().stream()
                .filter(n -> n.id() == id)
                .findFirst();
    }

    /**
     * Returns a notification by its name. If the notification does not exist, an empty optional is returned.
     *
     * @param name the name of the notification
     * @return the notification with the given name or an empty optional
     */
    default Optional<Notification> notification(String name) {
        return notifications().stream()
                .filter(n -> n.name().equals(name))
                .findFirst();
    }

    /**
     * Deletes a notification.
     *
     * @param notification the notification to delete
     */
    void deleteNotification(Notification notification);

    /**
     * Deletes a notification by its id.
     *
     * @param id the id of the notification
     */
    default void deleteNotification(int id) {
        notification(id).ifPresent(this::deleteNotification);
    }

    /**
     * Deletes a notification by its name.
     *
     * @param name the name of the notification
     */
    default void deleteNotification(String name) {
        notification(name).ifPresent(this::deleteNotification);
    }

    /**
     * Checks if a notification is active for a user.
     *
     * @param notification the notification to check
     * @param uuid         the uuid of the user
     * @return true if the notification is active for the user, otherwise false
     */
    boolean notificationActive(Notification notification, UUID uuid);

    /**
     * Sets a notification active or inactive for a user.
     *
     * @param notification the notification to set active or inactive
     * @param uuid         the uuid of the user
     * @param active       true to set the notification active, false to set it inactive
     */
    void notificationActive(Notification notification, UUID uuid, boolean active);
}