package de.keeeks.nucleo.modules.translation.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;
import de.keeeks.nucleo.modules.translation.spigot.registry.GlobalSpigotTranslationRegistry;
import lombok.Getter;

@Getter
@ModuleDescription(
        name = "translations",
        description = "The spigot translations module",
        depends = {"config"}
)
public class SpigotTranslationsModule extends SpigotModule {
    private TranslationRegistry translationRegistry;

    @Override
    public void load() {
        translationRegistry = new GlobalSpigotTranslationRegistry();
    }
}