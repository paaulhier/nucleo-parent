package de.keeeks.nucleo.modules.moderation.tools.velocity.commands.player;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.lejet.api.NameColorizer;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.moderation.tools.velocity.ModerationToolsVelocityModule;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.util.UUID;

@Command({"jumpto", "jt", "goto"})
@CommandPermission("nucleo.commands.jumpto")
@RequiredArgsConstructor
public class JumpToCommand {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final VelocityModule module = Module.module(ModerationToolsVelocityModule.class);

    @AutoComplete("players")
    @Usage("jumpto <player>")
    @DefaultFor({"jumpto", "jt", "goto"})
    public void jumpToCommand(
            Player player,
            String playerName
    ) {
        playerService.onlinePlayer(playerName).ifPresentOrElse(
                nucleoOnlinePlayer -> connectPlayerToServer(player, nucleoOnlinePlayer.uuid(), nucleoOnlinePlayer),
                () -> player.sendMessage(Component.translatable("playerNotFound").arguments(
                        Component.text(playerName)
                ))
        );
    }

    private void connectPlayerToServer(Player player, UUID targetUUID, NucleoOnlinePlayer nucleoOnlinePlayer) {
        Component nameComponent = NameColorizer.coloredName(targetUUID);
        module.proxyServer().getServer(nucleoOnlinePlayer.server()).ifPresentOrElse(
                registeredServer -> player.createConnectionRequest(
                        registeredServer
                ).connect().thenAccept(result -> {
                    if (result.isSuccessful()) {
                        player.sendMessage(Component.translatable(
                                "nucleo.commands.jumpto.success"
                        ).arguments(nameComponent));
                        return;
                    }
                    player.sendMessage(Component.translatable(
                            "nucleo.commands.jumpto.failed"
                    ).arguments(nameComponent));
                }),
                () -> player.sendMessage(Component.translatable(
                        "nucleo.commands.jumpto.unknownServer"
                ).arguments(nameComponent))
        );
    }

}