package de.keeeks.nucleo.modules.scoreboard.spigot;

import de.keeeks.nucleo.modules.scoreboard.api.Scoreboard;
import de.keeeks.nucleo.modules.scoreboard.api.ScoreboardApi;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
public class NucleoScoreboardApi implements ScoreboardApi {
    private final List<Scoreboard> scoreboards = new LinkedList<>();

    @Override
    public Scoreboard createScoreboard(org.bukkit.scoreboard.Scoreboard legacyBoard) {
        throw new UnsupportedOperationException("This method is deprecated and will be removed in the future. Please use createScoreboard(Player player) instead.");
    }

    @Override
    public List<Scoreboard> scoreboards() {
        return List.copyOf(scoreboards);
    }

    @Override
    public Scoreboard createScoreboard(Player player) {
        NucleoScoreboard nucleoScoreboard = new NucleoScoreboard(player);
        scoreboards.add(nucleoScoreboard);
        return nucleoScoreboard;
    }

    @Override
    public void destroyScoreboard(UUID playerId) {
        for (Scoreboard scoreboard : List.copyOf(scoreboards)) {
            if (scoreboard.playerId().equals(playerId)) {
                scoreboard.destroy();
                scoreboards.remove(scoreboard);
            }
        }
    }
}