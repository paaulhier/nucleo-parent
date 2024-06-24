package de.keeeks.nucleo.modules.scoreboard.api.lines;

import net.kyori.adventure.text.Component;

public interface StaticScoreboardLine extends ScoreboardLine {

    /**
     * Returns the component of this line.
     *
     * @return the component of this line
     */
    Component component();

}