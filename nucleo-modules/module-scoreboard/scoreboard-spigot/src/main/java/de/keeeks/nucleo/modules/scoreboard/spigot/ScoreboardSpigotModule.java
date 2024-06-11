package de.keeeks.nucleo.modules.scoreboard.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.scoreboard.api.Scoreboard;
import de.keeeks.nucleo.modules.scoreboard.api.ScoreboardApi;
import de.keeeks.nucleo.modules.scoreboard.api.lines.AnimatedScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.api.lines.AutoUpdatingScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.spigot.listener.ScoreboardPlayerQuitListener;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

@ModuleDescription(name = "scoreboard")
public class ScoreboardSpigotModule extends SpigotModule {
    private ScoreboardApi scoreboardApi;

    @Override
    public void load() {
        this.scoreboardApi = ServiceRegistry.registerService(
                ScoreboardApi.class,
                new NucleoScoreboardApi()
        );
    }

    @Override
    public void enable() {
        registerListener(new ScoreboardPlayerQuitListener(scoreboardApi));
        initializeScheduler();
    }

    private void initializeScheduler() {
        Bukkit.getAsyncScheduler().runAtFixedRate(
                plugin(),
                scheduledTask -> scoreboardApi.scoreboards().forEach(this::tickAllLines),
                0,
                50,
                TimeUnit.MILLISECONDS
        );
    }

    private void tickAllLines(Scoreboard scoreboard) {
        scoreboard.lines(AnimatedScoreboardLine.class).forEach(AnimatedScoreboardLine::tickAndRender);
        scoreboard.lines(AutoUpdatingScoreboardLine.class).forEach(AutoUpdatingScoreboardLine::tickAndRender);
    }
}