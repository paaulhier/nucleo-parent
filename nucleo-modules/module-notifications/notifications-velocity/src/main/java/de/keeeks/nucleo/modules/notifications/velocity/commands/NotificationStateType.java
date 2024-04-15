package de.keeeks.nucleo.modules.notifications.velocity.commands;

public enum NotificationStateType {
    ALL,
    ENABLED,
    DISABLED;

    public static NotificationStateType byName(String name) {
        for (NotificationStateType value : values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return ALL;
    }
}