package de.keeeks.nucleo.modules.scoreboard.spigot.lines;

import de.keeeks.nucleo.core.spigot.NucleoSpigotPlugin;
import de.keeeks.nucleo.modules.scoreboard.api.lines.ScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.spigot.NucleoScoreboard;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Score;

import java.util.concurrent.atomic.AtomicInteger;

import static net.kyori.adventure.text.Component.text;

@Getter
public abstract class NucleoScoreboardLine implements ScoreboardLine {
    private static final Plugin plugin = NucleoSpigotPlugin.plugin();

    private final NucleoScoreboard scoreboard;
    private final int index;

    public NucleoScoreboardLine(NucleoScoreboard scoreboard, int index) {
        this.scoreboard = scoreboard;
        this.index = index;
    }

    @Override
    public final void render() {
        scoreboard.fastBoard().updateLine(
                index,
                currentComponent()
        );
    }
}