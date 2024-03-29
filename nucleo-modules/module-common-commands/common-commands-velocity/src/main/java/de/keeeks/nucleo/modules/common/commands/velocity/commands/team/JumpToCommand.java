package de.keeeks.nucleo.modules.common.commands.velocity.commands.team;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.common.commands.velocity.CommonCommandsVelocityModule;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.velocity.annotation.CommandPermission;

@Command({"jumpto", "jt", "goto"})
@CommandPermission("nucleo.commands.jumpto")
@RequiredArgsConstructor
public class JumpToCommand {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final CommonCommandsVelocityModule module = Module.module(
            CommonCommandsVelocityModule.class
    );

    @AutoComplete("players")
    @Usage("jumpto <player>")
    @DefaultFor({"jumpto", "jt", "goto"})
    public void jumpToCommand(
            Player player,
            String playerName
    ) {
        playerService.onlinePlayer(playerName).ifPresentOrElse(
                nucleoOnlinePlayer -> connectPlayerToServer(player, playerName, nucleoOnlinePlayer),
                () -> player.sendMessage(Component.translatable("playerNotFound").arguments(
                        Component.text(playerName)
                ))
        );
    }

    private void connectPlayerToServer(Player player, String playerName, NucleoOnlinePlayer nucleoOnlinePlayer) {
        module.proxyServer().getServer(nucleoOnlinePlayer.server()).ifPresentOrElse(
                registeredServer -> player.createConnectionRequest(
                        registeredServer
                ).connect().thenAccept(result -> {
                    if (result.isSuccessful()) {
                        player.sendMessage(Component.translatable(
                                "nucleo.commands.jumpto.success"
                        ).arguments(
                                Component.text(playerName)
                        ));
                        return;
                    }
                    player.sendMessage(Component.translatable(
                            "nucleo.commands.jumpto.failed"
                    ).arguments(
                            Component.text(playerName)
                    ));
                }),
                () -> player.sendMessage(Component.translatable(
                        "nucleo.commands.jumpto.unknownServer"
                ).arguments(
                        Component.text(playerName)
                ))
        );
    }

}