package de.keeeks.nucleo.modules.translation.spigot.command;

import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.BukkitCommandActor;

public class TestCommand {

    @Command("test")
    public void test(
            final BukkitCommandActor actor
    ) {
        actor.audience().sendMessage(Component.translatable(
                "default.missing"
        ).args(Component.text("REPLACEMENT ALDA")));
    }
}