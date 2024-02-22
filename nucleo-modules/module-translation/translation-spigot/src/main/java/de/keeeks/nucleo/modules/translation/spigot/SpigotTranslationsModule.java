package de.keeeks.nucleo.modules.translation.spigot;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;
import de.keeeks.nucleo.modules.translation.spigot.command.LanguageCommand;
import de.keeeks.nucleo.modules.translation.spigot.command.TestCommand;
import de.keeeks.nucleo.modules.translation.spigot.registry.GlobalSpigotTranslationRegistry;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@ModuleDescription(
        name = "translations",
        description = "The proxy translations module",
        depends = {"config"},
        softDepends = {"players"}
)
public class SpigotTranslationsModule extends SpigotModule {
    private TranslationRegistry translationRegistry;

    @Override
    public void load() {
        translationRegistry = new GlobalSpigotTranslationRegistry();
        registerCommands(new TestCommand());
    }

    @Override
    public void enable() {
        Module playersModule = Module.module("players");
        if (playersModule != null) {
            registerCommands(new LanguageCommand());
        }
    }
}