package de.keeeks.nucleo.modules.players.api;

import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.util.regex.Pattern;

import static net.kyori.adventure.text.Component.translatable;

@Getter
public enum ClientBrand {
    /**
     * The player is using the vanilla Minecraft client.
     */
    VANILLA(translatable("brand.vanilla")),
    /**
     * The player is using the LabyMod Client
     */
    LABY_MOD(translatable("brand.labymod"), Pattern.compile("^labymod$")),
    /**
     * The player is using the Lunar Client
     */
    LUNAR(translatable("brand.lunar"), Pattern.compile("^lunarclient:v[0-9]+\\.[0-9]+\\.[0-9]+-[0-9]+$")),
    /**
     * The player is using the Feather Client
     */
    FEATHER(translatable("brand.feather"), Pattern.compile("^Feather Fabric$"));

    private final Component displayName;
    private final Pattern pattern;

    ClientBrand(Component displayName, Pattern pattern) {
        this.displayName = displayName;
        this.pattern = pattern;
    }

    ClientBrand(Component displayName) {
        this.displayName = displayName;
        this.pattern = null;
    }

    public static ClientBrand fromString(String brand) {
        for (ClientBrand clientBrand : values()) {
            if (clientBrand.pattern == null) continue;
            if (clientBrand.pattern.matcher(brand).matches()) {
                return clientBrand;
            }
        }
        return VANILLA;
    }
}