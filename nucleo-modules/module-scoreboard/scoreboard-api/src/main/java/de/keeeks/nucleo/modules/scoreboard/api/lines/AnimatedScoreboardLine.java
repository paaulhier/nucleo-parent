package de.keeeks.nucleo.modules.scoreboard.api.lines;

public interface AnimatedScoreboardLine extends DynamicScoreboardLine {

    int tickInterval();

    boolean tick();

    default void tickAndRender() {
        if (tick()) {
            render();
        }
    }
}