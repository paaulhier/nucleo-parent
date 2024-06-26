package de.keeeks.nucleo.modules.scoreboard.api;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScoreboardApi {

    /**
     * Get a list of all scoreboards
     *
     * @return The list of scoreboards
     */
    List<Scoreboard> scoreboards();

    @Deprecated
    Scoreboard createScoreboard(org.bukkit.scoreboard.Scoreboard legacyBoard);

    /**
     * Create a new scoreboard for a player
     *
     * @param player The player to create the scoreboard for
     * @return The created scoreboard
     */
    Scoreboard createScoreboard(Player player);

    /**
     * Destroy a scoreboard by its owner's UUID and remove it from the list of scoreboards
     *
     * @param playerId The UUID of the owner of the scoreboard
     */
    void destroyScoreboard(UUID playerId);

    /**
     * Get the scoreboard of a player
     *
     * @param player The player to get the scoreboard of
     * @return The scoreboard or an empty optional if the player has no scoreboard
     */
    default Optional<Scoreboard> scoreboard(Player player) {
        return scoreboards().stream().filter(
                scoreboard -> scoreboard.playerId().equals(player.getUniqueId())
        ).findFirst();
    }
}