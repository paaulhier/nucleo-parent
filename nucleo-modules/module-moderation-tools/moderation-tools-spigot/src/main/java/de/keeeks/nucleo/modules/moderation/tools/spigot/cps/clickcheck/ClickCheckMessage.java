package de.keeeks.nucleo.modules.moderation.tools.spigot.cps.clickcheck;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.spigot.ModerationToolsSpigotModule;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Logger;

public class ClickCheckMessage {
    private final ClickCheckApi clickCheckApi = ServiceRegistry.service(
            ClickCheckApi.class
    );
    private final ModerationToolsSpigotModule module = Module.module(ModerationToolsSpigotModule.class);
    private final ClickCheckMessageRunnable messageRunnable = new ClickCheckMessageRunnable();

    private final Logger logger;

    private BukkitTask actionBarTask;

    public ClickCheckMessage(Logger logger) {
        this.logger = logger;
        clickCheckApi.createListener(clickCheckInformation -> {
            if (actionBarTask == null || actionBarTask.isCancelled()) {
                start();
            }
        });
        clickCheckApi.deleteListener(clickCheckInformation -> stopIfNoClickChecks());
    }

    public final void start() {
        stop();
        actionBarTask = Bukkit.getScheduler().runTaskTimerAsynchronously(
                module.plugin(),
                messageRunnable,
                20,
                20
        );
        logger.info("Started ClickCheckMessage task");
    }

    public final void stopIfNoClickChecks() {
        if (clickCheckApi.clickChecksCount() == 0) {
            stop();
        }
    }

    public final void stop() {
        if (actionBarTask != null && !actionBarTask.isCancelled()) {
            actionBarTask.cancel();
            actionBarTask = null;
            logger.info("Stopped ClickCheckMessage task");
        }
    }
}