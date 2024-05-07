package de.keeeks.nucleo.modules.scoreboard.spigot.listener;

import de.keeeks.nucleo.modules.scoreboard.api.Scoreboard;
import de.keeeks.nucleo.modules.scoreboard.api.ScoreboardApi;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class ScoreboardPlayerQuitListener implements Listener {
    private final ScoreboardApi scoreboardApi;

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        scoreboardApi.scoreboard(event.getPlayer()).map(
                Scoreboard::uuid
        ).ifPresent(scoreboardApi::destroyScoreboard);
    }
}