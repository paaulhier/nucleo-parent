package de.keeeks.nucleo.modules.scoreboard.spigot.lines;

import de.keeeks.nucleo.modules.scoreboard.api.lines.DynamicScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.spigot.NucleoScoreboard;
import net.kyori.adventure.text.Component;

import java.util.function.Supplier;

public class NucleoDynamicScoreboardLine extends NucleoScoreboardLine implements DynamicScoreboardLine {

    private final Supplier<Component> supplier;

    public NucleoDynamicScoreboardLine(NucleoScoreboard scoreboard, int index, Supplier<Component> supplier) {
        super(scoreboard, index);
        this.supplier = supplier;
    }

    @Override
    public Component currentComponent() {
        return supplier.get();
    }
}