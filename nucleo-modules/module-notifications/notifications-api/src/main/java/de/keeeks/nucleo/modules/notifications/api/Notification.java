package de.keeeks.nucleo.modules.notifications.api;

import java.time.Instant;
import java.util.function.Function;

public interface Notification {

    int id();

    String name();

    String description();

    String requiredPermission();

    Instant createdAt();

    Instant updatedAt();

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

    default void checkPermission(
            Function<String, Boolean> permissionCheckFunction,
            Runnable successRunnable
    ) {
        checkPermission(permissionCheckFunction, successRunnable, () -> {});
    }
}