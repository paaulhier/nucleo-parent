package de.keeeks.nucleo.modules.moderation.tools.velocity.commands.administration;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.lejet.api.NameColorizer;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.velocity.annotation.CommandPermission;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

@Command("pull")
@CommandPermission("nucleo.moderation.pull")
public class PullCommand {

    @Usage("nucleo.moderation.pull.usage")
    @AutoComplete("@players")
    @DefaultFor("pull")
    public void pullCommand(Player player, NucleoOnlinePlayer onlinePlayer) {
        if (onlinePlayer == null) {
            player.sendMessage(translatable("playerNotOnline"));
            return;
        }

        player.getCurrentServer().ifPresentOrElse(serverConnection -> onlinePlayer.connect(
                serverConnection.getServerInfo().getName(), state -> {
                    if (!state.successful()) {
                        player.sendMessage(translatable(
                                "nucleo.command.pull.failed",
                                text(state.name())
                        ));
                        return;
                    }
                    player.sendMessage(translatable(
                            "nucleo.command.pull.success",
                            NameColorizer.coloredName(onlinePlayer.uuid())
                    ));
                }
        ), () -> player.sendMessage(translatable(
                "nucleo.command.pull.notOnServer"
        )));
    }
}