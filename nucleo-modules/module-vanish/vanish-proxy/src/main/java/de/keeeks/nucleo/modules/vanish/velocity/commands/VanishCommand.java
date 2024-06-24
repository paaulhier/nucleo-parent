package de.keeeks.nucleo.modules.vanish.velocity.commands;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.lejet.api.NameColorizer;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.vanish.api.VanishApi;
import de.keeeks.nucleo.modules.vanish.api.VanishData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.util.UUID;

import static net.kyori.adventure.text.Component.translatable;

@Command({"vanish", "v"})
@CommandPermission("nucleo.commands.vanish")
public class VanishCommand {
    private final VanishApi vanishApi = ServiceRegistry.service(VanishApi.class);

    @DefaultFor("~")
    public void vanishCommand(
            Player player,
            @Optional NucleoOnlinePlayer targetPlayer
    ) {
        UUID uuid = targetPlayer == null ? player.getUniqueId() : targetPlayer.uuid();
        boolean self = targetPlayer == null || targetPlayer.uuid().equals(player.getUniqueId());

        VanishData vanishData = vanishApi.vanishData(uuid);
        if (vanishData.vanished(!vanishData.vanished())) {
            player.sendMessage(translatable(
                    "nucleo.vanish." + (self ? "self" : "other") + ".vanished",
                    NameColorizer.coloredName(uuid)
            ));
        } else {
            player.sendMessage(translatable(
                    "nucleo.vanish." + (self ? "self" : "other") + ".visible",
                    NameColorizer.coloredName(uuid)
            ));
        }
    }
}