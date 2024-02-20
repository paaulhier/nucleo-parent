package de.keeeks.nucleo.modules.translation.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;
import de.keeeks.nucleo.modules.translation.spigot.command.TestCommand;
import de.keeeks.nucleo.modules.translation.spigot.registry.GlobalSpigotTranslationRegistry;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.Locale;

@Getter
@Accessors(fluent = true)
@ModuleDescription(
        name = "translations",
        description = "The proxy translations module",
        softDepends = "config"
)
public class SpigotTranslationsModule extends SpigotModule {
    private TranslationRegistry translationRegistry;

    @Override
    public void load() {
        translationRegistry = new GlobalSpigotTranslationRegistry();
        registerCommands(new TestCommand());
    }
}