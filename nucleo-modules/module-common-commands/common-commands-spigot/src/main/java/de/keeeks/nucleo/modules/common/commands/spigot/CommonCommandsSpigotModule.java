package de.keeeks.nucleo.modules.common.commands.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.common.commands.api.translation.CommonCommandsTranslationRegistry;
import de.keeeks.nucleo.modules.common.commands.spigot.commands.BetterPasteLocationCommand;
import de.keeeks.nucleo.modules.common.commands.spigot.commands.FlyCommand;
import de.keeeks.nucleo.modules.common.commands.spigot.commands.ModulesCommand;
import de.keeeks.nucleo.modules.common.commands.spigot.commands.UptimeCommand;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;

@ModuleDescription(
        name = "common-commands",
        depends = {"translations", "messaging"}
)
public class CommonCommandsSpigotModule extends SpigotModule {

    @Override
    public void load() {
        TranslationRegistry.initializeRegistry(new CommonCommandsTranslationRegistry(
                this
        ));
    }

    @Override
    public void enable() {
        registerCommands(
                new BetterPasteLocationCommand(),
                new ModulesCommand(),
                new UptimeCommand(plugin()),
                new FlyCommand()
        );
    }
}