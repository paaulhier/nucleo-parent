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

    DynamicScoreboardLine dynamicLine(Supplier<Component> component);

    @Deprecated
    default DynamicScoreboardLine dynamicLine(int index, Supplier<Component> supplier) {
        return dynamicLine(supplier);
    }

    ScoreboardLine staticLine(Component component);

    @Deprecated
    default ScoreboardLine staticLine(int index, Component component) {
        return staticLine(component);
    }

    @Deprecated
    default AnimatedScoreboardLine animatedLine(int index, int tickInterval, List<Component> lines) {
        return animatedLine(tickInterval, lines);
    }

    AnimatedScoreboardLine animatedLine(int tickInterval, List<Component> lines);

    default AnimatedScoreboardLine animatedLineWithDefaultTicking(int index, List<Component> lines) {
        return animatedLine(index, 20, lines);
    }

    void renderAll();

    void addPlayer(Player player);

    void destroy();

}