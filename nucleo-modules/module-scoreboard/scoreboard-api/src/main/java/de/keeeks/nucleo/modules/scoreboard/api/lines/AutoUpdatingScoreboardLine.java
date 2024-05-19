package de.keeeks.nucleo.modules.scoreboard.api.lines;

public interface AutoUpdatingScoreboardLine extends DynamicScoreboardLine{

    int tickInterval();

    boolean tick();

    default void tickAndRender() {
        if (tick()) {
            render();
        }
    }
}