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

    /**
     * Get a scoreboard by its UUID
     * @param uuid The UUID of the scoreboard
     * @return The scoreboard or an empty optional if no scoreboard with the given UUID exists
     */
    default Optional<Scoreboard> scoreboard(UUID uuid) {
        return scoreboards().stream().filter(
                scoreboard -> scoreboard.uuid().equals(uuid)
        ).findFirst();
    }

    /**
     * Get the scoreboard of a player
     * @param player The player to get the scoreboard of
     * @return The scoreboard or an empty optional if the player has no scoreboard
     */
    default Optional<Scoreboard> scoreboard(Player player) {
        return player.getMetadata("scoreboardId").stream().findFirst().flatMap(
                metadataValue -> scoreboard(UUID.fromString(metadataValue.asString()))
        );
    }
}