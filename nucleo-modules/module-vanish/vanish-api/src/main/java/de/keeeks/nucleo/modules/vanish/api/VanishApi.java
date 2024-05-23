package de.keeeks.nucleo.modules.vanish.api;

import java.util.UUID;

public interface VanishApi {
    String CHANNEL = "nucleo:vanish";

    VanishData vanishData(UUID uuid);

    void invalidate(UUID uuid);
}