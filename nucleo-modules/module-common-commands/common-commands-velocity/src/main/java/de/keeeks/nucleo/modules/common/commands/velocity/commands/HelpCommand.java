package de.keeeks.nucleo.modules.common.commands.velocity.commands;

import com.velocitypowered.api.proxy.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;

import static net.kyori.adventure.text.Component.translatable;

@Command({"help", "?", "about"})
public class HelpCommand {

    @DefaultFor("~")
    public void helpCommand(Player player) {
        player.sendMessage(translatable("nucleo.commands.help"));
    }
}