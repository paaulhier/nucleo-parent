package de.keeeks.nucleo.modules.scoreboard.api;

import de.keeeks.nucleo.modules.scoreboard.api.lines.AnimatedScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.api.lines.DynamicScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.api.lines.ScoreboardLine;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public interface Scoreboard {

    UUID uuid();

    Scoreboard title(Component title);

    List<ScoreboardLine> lines();

    default <T> List<T> lines(Class<T> type) {
        return lines().stream().filter(type::isInstance).map(type::cast).toList();
    }

    DynamicScoreboardLine dynamicLine(int index, Supplier<Component> supplier);

    ScoreboardLine staticLine(int index, Component component);

    AnimatedScoreboardLine animatedLine(int index, int tickInterval, List<Component> lines);

    default AnimatedScoreboardLine animatedLine(int index, List<Component> lines) {
        return animatedLine(index, 20, lines);
    }

    void renderAll();

    void addPlayer(Player player);

}