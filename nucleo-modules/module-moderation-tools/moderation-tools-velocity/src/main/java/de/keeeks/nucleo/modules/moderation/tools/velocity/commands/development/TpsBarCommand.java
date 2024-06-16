package de.keeeks.nucleo.modules.moderation.tools.velocity.commands.development;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.moderation.tools.tpsbar.TpsBarApi;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.velocity.annotation.CommandPermission;

import static net.kyori.adventure.text.Component.translatable;

@Command("tpsbar")
@CommandPermission("nucleo.command.development.tpsbar")
public final class TpsBarCommand {
    private final TpsBarApi tpsBarApi = ServiceRegistry.service(TpsBarApi.class);

    @DefaultFor("tpsbar")
    public void tpsBar(Player player) {
        if (tpsBarApi.toggle(player.getUniqueId())) {
            player.sendMessage(translatable("nucleo.command.development.tpsbar.enabled"));
        } else {
            player.sendMessage(translatable("nucleo.command.development.tpsbar.disabled"));
        }
    }
}