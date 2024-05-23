package de.keeeks.nucleo.modules.vanish.api;

import java.util.UUID;

public interface VanishData {
    UUID uuid();
    boolean vanished();
    boolean vanished(boolean vanished);
}