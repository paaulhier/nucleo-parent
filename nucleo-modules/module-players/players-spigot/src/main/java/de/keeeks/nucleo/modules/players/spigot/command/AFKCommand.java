package de.keeeks.nucleo.modules.players.spigot.command;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.modules.players.api.OnlineState;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.spigot.afk.AFKService;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;

@Command({"away", "afk"})
public class AFKCommand {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final AFKService afkService = ServiceRegistry.service(AFKService.class);

    @DefaultFor({"away", "afk"})
    public void afkCommand(Player player) {
        Scheduler.runAsync(() -> playerService.onlinePlayer(player.getUniqueId()).ifPresent(
                onlinePlayer -> afkService.forceChangeState(
                        player.getUniqueId(),
                        onlinePlayer,
                        onlinePlayer.onlineState() == OnlineState.AWAY ? OnlineState.ONLINE : OnlineState.AWAY
                )
        ));
    }
}