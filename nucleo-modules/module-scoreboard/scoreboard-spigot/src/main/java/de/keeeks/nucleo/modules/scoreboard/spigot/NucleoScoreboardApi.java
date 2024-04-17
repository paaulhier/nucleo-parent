package de.keeeks.nucleo.modules.scoreboard.spigot;

import de.keeeks.nucleo.modules.scoreboard.api.Scoreboard;
import de.keeeks.nucleo.modules.scoreboard.api.ScoreboardApi;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
public class NucleoScoreboardApi implements ScoreboardApi {
    private final List<Scoreboard> scoreboards = new LinkedList<>();

    @Override
    public Scoreboard createScoreboard() {
        NucleoScoreboard nucleoScoreboard = new NucleoScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        scoreboards.add(nucleoScoreboard);
        return nucleoScoreboard;
    }

    @Override
    public Scoreboard createScoreboard(org.bukkit.scoreboard.Scoreboard legacyBoard) {
        NucleoScoreboard nucleoScoreboard = new NucleoScoreboard(legacyBoard);
        scoreboards.add(nucleoScoreboard);
        return nucleoScoreboard;
    }

    @Override
    public Scoreboard createScoreboard(Player player) {
        NucleoScoreboard nucleoScoreboard = new NucleoScoreboard(player);
        scoreboards.add(nucleoScoreboard);
        return nucleoScoreboard;
    }

    @Override
    public void destroyScoreboard(UUID uuid) {
        scoreboards.removeIf(scoreboard -> scoreboard.uuid().equals(uuid));
    }
}