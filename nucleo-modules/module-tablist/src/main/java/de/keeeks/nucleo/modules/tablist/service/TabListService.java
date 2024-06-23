package de.keeeks.nucleo.modules.tablist.service;

import de.keeeks.nucleo.modules.tablist.event.PlayerTabListForPlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import static de.keeeks.nucleo.modules.tablist.event.PlayerTabListForPlayerEvent.TargetType.NAME_TAG;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.space;

public final class TabListService {

    public void updateTabList() {
        Bukkit.getOnlinePlayers().forEach(this::setTabListForPlayer);
    }

    public void setTabListForPlayer(Player player) {
        Scoreboard scoreboard = scoreboardByPlayer(player);

        // Clear the current tab list

        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            PlayerTabListForPlayerEvent event = new PlayerTabListForPlayerEvent(otherPlayer, player, NAME_TAG);

            // Call the event
            Bukkit.getPluginManager().callEvent(event);

            // If the event is cancelled, skip the player
            if (event.isCancelled()) continue;

            // Get or create the team
            Team team = getOrCreateTeam(scoreboard, event);

            // Add the player to the team
            team.addPlayer(otherPlayer);

            // Set the prefix and suffix
            if (!event.prefix().equals(empty())) {
                team.prefix(event.prefix());
            }
            if (!event.suffix().equals(empty())) {
                team.suffix(space().append(event.suffix()));
            }
            team.color(event.color());

            // Set the options for the team
            team.setAllowFriendlyFire(event.allowFriendlyFire());
            team.setCanSeeFriendlyInvisibles(event.canSeeFriendlyInvisibles());
            event.options().forEach(team::setOption);
        }
    }

    private Team getOrCreateTeam(Scoreboard scoreboard, PlayerTabListForPlayerEvent event) {
        String teamName = event.priority() + "_" + event.viewer().getName();
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }
        return team;
    }

    private Scoreboard scoreboardByPlayer(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard.equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            player.setScoreboard(scoreboard);
        }
        return scoreboard;
    }
}