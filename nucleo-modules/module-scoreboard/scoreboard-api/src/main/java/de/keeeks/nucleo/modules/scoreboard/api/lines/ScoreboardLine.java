package de.keeeks.nucleo.modules.scoreboard.api.lines;

import net.kyori.adventure.text.Component;

public interface ScoreboardLine {

    /**
     * Returns the index of this scoreboard line.
     * @return the index of this scoreboard line
     */
    int index();

    /**
     * Renders the line
     */
    void render();

    /**
     * Returns the component of this line.
     * @return the component of this line
     */
    Component currentComponent();

}