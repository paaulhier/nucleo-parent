package de.keeeks.nucleo.modules.notifications.api;

import java.time.Instant;
import java.util.function.Function;

public interface Notification {

    /**
     * The identifier of the notification.
     * @return the id of the notification
     */
    int id();

    /**
     * The name of the notification.
     * @return the name of the notification
     */
    String name();

    /**
     * The description of the notification.
     * @return the description of the notification
     */
    String description();

    /**
     * The required permission to see the notification.
     * @return the required permission
     */
    String requiredPermission();

    /**
     * The creation date of the notification.
     * @return the creation date
     */
    Instant createdAt();

    /**
     * The update date of the notification (last change).
     * @return the update date
     */
    Instant updatedAt();

    /**
     * Checks if the user has the required permission to see the notification.
     * @param permissionCheckFunction the function to check the permission
     * @param successRunnable the runnable to execute if the user has the permission
     * @param failureRunnable the runnable to execute if the user has not the permission
     */
    default void checkPermission(
            Function<String, Boolean> permissionCheckFunction,
            Runnable successRunnable,
            Runnable failureRunnable
    ) {
        if (requiredPermission() != null) {
            if (permissionCheckFunction.apply(requiredPermission())) {
                successRunnable.run();
            } else {
                failureRunnable.run();
            }
        } else {
            successRunnable.run();
        }
    }

    /**
     * Checks if the user has the required permission to see the notification.
     * @param permissionCheckFunction the function to check the permission
     * @param successRunnable the runnable to execute if the user has the permission
     */
    default void checkPermission(
            Function<String, Boolean> permissionCheckFunction,
            Runnable successRunnable
    ) {
        checkPermission(permissionCheckFunction, successRunnable, () -> {
        });
    }
}