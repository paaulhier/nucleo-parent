package de.keeeks.nucleo.modules.scoreboard.api;

import de.keeeks.nucleo.modules.scoreboard.api.lines.AnimatedScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.api.lines.AutoUpdatingScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.api.lines.DynamicScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.api.lines.ScoreboardLine;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public interface Scoreboard {

    /**
     * Returns the unique identifier of the owner of this scoreboard.
     *
     * @return the unique identifier of the owner of this scoreboard
     */
    UUID playerId();

    /**
     * Sets the title of the scoreboard.
     *
     * @param title the title of the scoreboard
     * @return the scoreboard instance
     */
    Scoreboard title(Component title);

    /**
     * Returns all registered lines of this scoreboard.
     *
     * @return all registered lines of this scoreboard
     */
    List<ScoreboardLine> lines();

    /**
     * Returns all registered lines of this scoreboard that are of the given type.
     *
     * @param type the type of the lines
     * @param <T>  the type of the lines e.g. {@link AnimatedScoreboardLine}, {@link AutoUpdatingScoreboardLine}, {@link DynamicScoreboardLine} or {@link ScoreboardLine}
     * @return all registered lines of this scoreboard that are of the given type
     */
    default <T> List<T> lines(Class<T> type) {
        return lines().stream().filter(type::isInstance).map(type::cast).toList();
    }

    /**
     * Returns the line at the given index.
     *
     * @param tickInterval the tick interval for the auto updating line
     * @param component    the component supplier
     * @return the auto updating scoreboard line
     */
    AutoUpdatingScoreboardLine autoUpdatingLine(int tickInterval, Supplier<Component> component);

    /**
     * Creates a new dynamic line with the given component supplier.
     * <p>
     * Dynamic lines are lines that are rendered every time the scoreboard is rendered.
     *
     * @param component the component supplier
     * @return the dynamic scoreboard line
     */
    DynamicScoreboardLine dynamicLine(Supplier<Component> component);

    @Deprecated
    default DynamicScoreboardLine dynamicLine(int index, Supplier<Component> supplier) {
        return dynamicLine(supplier);
    }

    /**
     * Creates a new static line with the given component.
     *
     * @param component the content of the line
     * @return the static scoreboard line
     */
    ScoreboardLine staticLine(Component component);

    @Deprecated
    default ScoreboardLine staticLine(int index, Component component) {
        return staticLine(component);
    }

    @Deprecated
    default AnimatedScoreboardLine animatedLine(int index, int tickInterval, List<Component> lines) {
        return animatedLine(tickInterval, lines);
    }

    /**
     * Creates a new animated line with the given tick interval and lines.
     *
     * @param tickInterval the tick interval for the animation
     * @param lines        the lines of the animation
     * @return the animated scoreboard line
     */
    default AnimatedScoreboardLine animatedLine(int tickInterval, List<Component> lines) {
        return animatedLine(tickInterval, () -> lines);
    }

    /**
     * Creates a new animated line with the given tick interval and lines.
     *
     * @param tickInterval the tick interval for the animation
     * @param lines        the lines of the animation
     * @return the animated scoreboard line
     */
    AnimatedScoreboardLine animatedLine(int tickInterval, Supplier<List<Component>> lines);

    /**
     * Creates a new animated line with the given tick interval and lines and a default ticking of 20 ticks.
     *
     * @param index the index of the line
     * @param lines the lines of the animation
     * @return the animated scoreboard line
     */
    default AnimatedScoreboardLine animatedLineWithDefaultTicking(int index, List<Component> lines) {
        return animatedLine(index, 20, lines);
    }

    /**
     * Renders all the lines of the scoreboard.
     */
    void renderAll();

    /**
     * Destroys the scoreboard and removes all lines.
     * <p>
     * This method should be called when the scoreboard is no longer needed.
     * It will remove all lines and unregister the scoreboard from the {@link ScoreboardApi}.
     * After calling this method, the scoreboard should no longer be used.
     * If the scoreboard is still used after calling this method, it may lead to unexpected behavior.
     * </p>
     */
    void destroy();

}