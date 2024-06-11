package de.keeeks.nucleo.modules.scoreboard.api.lines;

public interface AutoUpdatingScoreboardLine extends DynamicScoreboardLine {

    /**
     * Returns the tick interval of this auto updating scoreboard line.
     *
     * @return the tick interval of this auto updating scoreboard line
     */
    int tickInterval();

    /**
     * Checks if the line should be rendered.
     *
     * @return {@code true} if the line should be rendered, otherwise {@code false}
     */
    boolean tick();

    /**
     * Checks if the line should be rendered and renders it if necessary.
     * <p>
     * This method is a combination of {@link #tick()} and {@link #render()}.
     * If {@link #tick()} returns {@code true}, the line will be rendered.
     * Otherwise, the line will not be rendered.
     * </p>
     */
    default void tickAndRender() {
        if (tick()) {
            render();
        }
    }
}