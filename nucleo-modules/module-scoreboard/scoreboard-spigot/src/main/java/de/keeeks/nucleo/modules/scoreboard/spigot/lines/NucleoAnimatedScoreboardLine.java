package de.keeeks.nucleo.modules.scoreboard.spigot.lines;

import de.keeeks.nucleo.modules.scoreboard.api.lines.AnimatedScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.spigot.NucleoScoreboard;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NucleoAnimatedScoreboardLine extends NucleoScoreboardLine implements AnimatedScoreboardLine {
    private final AtomicInteger index = new AtomicInteger(0);
    private final AtomicLong lastUpdate = new AtomicLong(0);

    private final int tickInterval;
    private final List<Component> lines;

    public NucleoAnimatedScoreboardLine(NucleoScoreboard scoreboard, int index, int tickInterval, List<Component> lines) {
        super(scoreboard, index);
        this.tickInterval = tickInterval;
        this.lines = lines;
    }

    @Override
    public int tickInterval() {
        return tickInterval;
    }

    @Override
    public boolean tick() {
        if (System.currentTimeMillis() - lastUpdate.get() < ticksToMilliseconds()) return false;

        int currentIndex = index.getAndIncrement();

        if (currentIndex >= (lines.size() - 1)) {
            index.set(0);
        }
        lastUpdate.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public Component currentComponent() {
        return lines.get(index.get());
    }

    private int ticksToMilliseconds() {
        return tickInterval * 50;
    }
}