package de.keeeks.nucleo.modules.moderation.tools.spigot.cps.clickcheck;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.spigot.ModerationToolsSpigotModule;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class ClickCheckMessage {
    private final ClickCheckApi clickCheckApi = ServiceRegistry.service(
            ClickCheckApi.class
    );
    private final ModerationToolsSpigotModule module = Module.module(ModerationToolsSpigotModule.class);
    private final ClickCheckMessageRunnable messageRunnable = new ClickCheckMessageRunnable();

    private BukkitTask actionBarTask;

    public ClickCheckMessage() {
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
        }
    }
}