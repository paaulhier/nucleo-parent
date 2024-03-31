package de.keeeks.nucleo.modules.scoreboard.spigot.lines;

import de.keeeks.nucleo.modules.scoreboard.api.lines.StaticScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.spigot.NucleoScoreboard;
import lombok.Getter;
import net.kyori.adventure.text.Component;

@Getter
public class NucleoStaticScoreboardLine extends NucleoScoreboardLine implements StaticScoreboardLine {
    private final Component component;

    public NucleoStaticScoreboardLine(NucleoScoreboard scoreboard, int index, Component component) {
        super(scoreboard, index);
        this.component = component;
    }

    @Override
    public Component currentComponent() {
        return component;
    }
}
