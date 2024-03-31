package de.keeeks.nucleo.modules.scoreboard.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.scoreboard.api.Scoreboard;
import de.keeeks.nucleo.modules.scoreboard.api.ScoreboardApi;
import de.keeeks.nucleo.modules.scoreboard.api.lines.AnimatedScoreboardLine;
import org.bukkit.Bukkit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@ModuleDescription(
        name = "scoreboard"
)
public class ScoreboardSpigotModule extends SpigotModule {
    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(
            r -> new Thread(r, "Scoreboard-Executor")
    );

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
        initializeScheduler();
    }

    private void initializeScheduler() {
        AtomicInteger ticked = new AtomicInteger(0);

        Bukkit.getAsyncScheduler().runAtFixedRate(
                plugin(),
                scheduledTask -> {
                    scoreboardApi.scoreboards().forEach(
                            scoreboard -> tickAllAnimatedLines(scoreboard, ticked)
                    );
                },
                0,
                50,
                TimeUnit.MILLISECONDS
        );
    }

    private void tickAllAnimatedLines(Scoreboard scoreboard, AtomicInteger ticked) {
        scoreboard.lines(AnimatedScoreboardLine.class).forEach(AnimatedScoreboardLine::tickAndRender);
    }
}