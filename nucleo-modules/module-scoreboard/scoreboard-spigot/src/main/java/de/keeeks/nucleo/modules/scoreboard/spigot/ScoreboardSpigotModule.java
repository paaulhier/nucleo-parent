package de.keeeks.nucleo.modules.scoreboard.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.scoreboard.api.Scoreboard;
import de.keeeks.nucleo.modules.scoreboard.api.ScoreboardApi;
import de.keeeks.nucleo.modules.scoreboard.api.lines.AnimatedScoreboardLine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

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
        registerListener(new Listener() {

            @EventHandler
            public void join(PlayerJoinEvent event) {
                Player player = event.getPlayer();

                Scoreboard scoreboard = scoreboardApi.createScoreboard().title(
                        text("Test Scoreboard", Style.style(NamedTextColor.DARK_RED))
                );
                scoreboard.dynamicLine(
                        1,
                        () -> text("Hello World")
                );
                scoreboard.staticLine(
                        5,
                        text("Static Line")
                );
                scoreboard.animatedLine(
                        2,
                        40,
                        List.of(
                                translatable("line.1"),
                                translatable("line.2"),
                                translatable("line.3")
                        )
                );

                scoreboard.animatedLine(
                        15,
                        10,
                        List.of(
                                text("Eripferd ist cool", Style.style(
                                        NamedTextColor.RED
                                )),
                                text("Eripferd ist kacke", Style.style(
                                        NamedTextColor.BLUE
                                )),
                                text("Eripferd ist doof", Style.style(
                                        NamedTextColor.GREEN
                                ))
                        )
                );

                scoreboard.addPlayer(player);
                scoreboard.renderAll();
                player.sendMessage(text("Scoreboard created!"));
            }
        });
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