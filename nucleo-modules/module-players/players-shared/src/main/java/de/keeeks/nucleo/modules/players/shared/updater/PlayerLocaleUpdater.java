package de.keeeks.nucleo.modules.players.shared.updater;

import de.keeeks.nucleo.core.api.Module;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.logging.Level;

public final class PlayerLocaleUpdater {
    private final Module module;
    private Field localeField;

    public PlayerLocaleUpdater(Class<?> playerClass, Module module) {
        this(playerClass, module, "locale");
    }

    public PlayerLocaleUpdater(Class<?> playerClass, Module module, String fieldName) {
        this.module = module;
        try {
            localeField = playerClass.getDeclaredField(fieldName);
            localeField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            module.logger().log(
                    Level.SEVERE,
                    "Failed to initialize PlayerLocaleUpdater",
                    e
            );
        }
    }

    public void updateLocale(Object player, Locale locale) {
        if (localeField == null) {
            module.logger().log(
                    Level.SEVERE,
                    "Failed to update locale for player " + player + " because the locale field is not initialized"
            );
            return;
        }

        try {
            localeField.set(
                    player,
                    locale
            );
        } catch (IllegalAccessException e) {
            module.logger().log(
                    Level.SEVERE,
                    "Failed to update locale for player " + player,
                    e
            );
        }
    }
}