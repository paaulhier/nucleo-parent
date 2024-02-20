package de.keeeks.nucleo.modules.translation.spigot.registry;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;
import de.keeeks.nucleo.modules.translation.spigot.SpigotTranslationsModule;

public class GlobalSpigotTranslationRegistry extends TranslationRegistry {

    public GlobalSpigotTranslationRegistry() {
        super(Module.module(SpigotTranslationsModule.class));
    }
}