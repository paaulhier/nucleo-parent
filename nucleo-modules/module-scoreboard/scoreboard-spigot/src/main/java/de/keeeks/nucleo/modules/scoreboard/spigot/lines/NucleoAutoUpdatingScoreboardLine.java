package de.keeeks.nucleo.modules.scoreboard.spigot.lines;

import de.keeeks.nucleo.modules.scoreboard.api.lines.AutoUpdatingScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.spigot.NucleoScoreboard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

@Getter
public class NucleoAutoUpdatingScoreboardLine extends NucleoDynamicScoreboardLine implements AutoUpdatingScoreboardLine {

    private final AtomicLong lastUpdate = new AtomicLong(0);

    private final int tickInterval;

    public NucleoAutoUpdatingScoreboardLine(
            NucleoScoreboard scoreboard,
            int index,
            int tickInterval,
            Supplier<Component> componentSupplier
    ) {
        super(scoreboard, index, componentSupplier);
        this.tickInterval = tickInterval;
    }


    @Override
    public boolean tick() {
        if (System.currentTimeMillis() - lastUpdate.get() < ticksToMilliseconds()) return false;

        lastUpdate.set(System.currentTimeMillis());
        return true;
    }

    private int ticksToMilliseconds() {
        return tickInterval * 50;
    }
}