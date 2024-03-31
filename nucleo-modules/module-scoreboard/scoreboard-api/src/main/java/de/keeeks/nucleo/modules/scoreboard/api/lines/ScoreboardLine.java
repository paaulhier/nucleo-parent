package de.keeeks.nucleo.modules.scoreboard.api.lines;

import net.kyori.adventure.text.Component;

public interface ScoreboardLine {

    int index();

    void render();

    Component currentComponent();

}