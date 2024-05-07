package de.keeeks.nucleo.modules.scoreboard.api;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScoreboardApi {

    List<Scoreboard> scoreboards();

    @Deprecated
    Scoreboard createScoreboard(org.bukkit.scoreboard.Scoreboard legacyBoard);

    Scoreboard createScoreboard(Player player);

    void destroyScoreboard(UUID uuid);

    default Optional<Scoreboard> scoreboard(UUID uuid) {
        return scoreboards().stream().filter(
                scoreboard -> scoreboard.uuid().equals(uuid)
        ).findFirst();
    }
}