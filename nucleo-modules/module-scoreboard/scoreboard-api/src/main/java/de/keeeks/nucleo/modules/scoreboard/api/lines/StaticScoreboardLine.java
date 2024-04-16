package de.keeeks.nucleo.modules.scoreboard.api.lines;

import net.kyori.adventure.text.Component;

public interface StaticScoreboardLine extends ScoreboardLine {

    Component component();

    void render();

}