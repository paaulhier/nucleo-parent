package de.keeeks.nucleo.modules.scoreboard.spigot;

import de.keeeks.nucleo.modules.scoreboard.api.Scoreboard;
import de.keeeks.nucleo.modules.scoreboard.api.ScoreboardApi;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.LinkedList;
import java.util.List;

@Getter
public class NucleoScoreboardApi implements ScoreboardApi {
    private final List<Scoreboard> scoreboards = new LinkedList<>();

    @Override
    public Scoreboard createScoreboard() {
        NucleoScoreboard nucleoScoreboard = new NucleoScoreboard();
        scoreboards.add(nucleoScoreboard);
        return nucleoScoreboard;
    }
}