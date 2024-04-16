package de.keeeks.nucleo.modules.scoreboard.spigot;

import de.keeeks.nucleo.modules.scoreboard.api.Scoreboard;
import de.keeeks.nucleo.modules.scoreboard.api.ScoreboardApi;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
public class NucleoScoreboardApi implements ScoreboardApi {
    private final List<Scoreboard> scoreboards = new LinkedList<>();

    @Override
    public Scoreboard createScoreboard() {
        NucleoScoreboard nucleoScoreboard = new NucleoScoreboard();
        scoreboards.add(nucleoScoreboard);
        return nucleoScoreboard;
    }

    @Override
    public void destroyScoreboard(UUID uuid) {
        scoreboards.removeIf(scoreboard -> scoreboard.uuid().equals(uuid));
    }
}