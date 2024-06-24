package de.keeeks.nucleo.modules.moderation.tools.spigot.tpsbar;

import de.keeeks.nucleo.core.spigot.NucleoSpigotPlugin;
import de.keeeks.nucleo.modules.moderation.tools.shared.tpsbar.NucleoTpsBarApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class SpigotNucleoTpsBarApi extends NucleoTpsBarApi {
    private static final NumberFormat decimalFormat = new DecimalFormat("0.00");

    public SpigotNucleoTpsBarApi(BukkitScheduler bukkitScheduler) {
        super();

        bukkitScheduler.runTaskTimerAsynchronously(NucleoSpigotPlugin.plugin(), bukkitTask -> {
            for (UUID uuid : enabledPlayers()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;

                player.sendActionBar(translatable(
                        "nucleo.tpsbar",
                        text(decimalFormat.format(tps(0))), // Last minute
                        text(decimalFormat.format(tps(1))), // Last 5 minutes
                        text(decimalFormat.format(tps(2))), // Last 15 minutes
                        text(decimalFormat.format(ramPercentage())), // RAM usage in %
                        text(decimalFormat.format(usedRam())), // Used RAM in MB
                        text(decimalFormat.format(totalAssignedRam())), // Total assigned RAM in MB
                        text(decimalFormat.format(averageCpuUsage())) // CPU usage in %
                ));
            }
        }, 0, 2);
    }

    /**
     * Returns the TPS value at the given index of the last minute, last 5 minutes or last 15 minutes.
     *
     * @param index The index of the TPS value
     * @return The TPS value or 0 if the index is out of bounds
     */
    private double tps(int index) {
        double[] tps = Bukkit.getTPS();

        if (index < 0 || index >= tps.length) {
            return 0;
        }

        return Math.min(20.0, tps[index]);
    }

    /**
     * Calculates the used RAM in MiB.
     *
     * @return The used RAM in MiB
     */
    private double usedRam() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0 / 1024.0;
    }

    /**
     * Calculates the total assigned RAM to the current process in MiB.
     *
     * @return The total assigned RAM in MiB
     */
    private double totalAssignedRam() {
        return Runtime.getRuntime().maxMemory() / 1024.0 / 1024.0;
    }

    /**
     * Calculates the percentage of used RAM based on the total assigned RAM.
     *
     * @return The percentage of used RAM
     */
    private double ramPercentage() {
        return usedRam() / totalAssignedRam() * 100;
    }

    /**
     * Calculates the average CPU usage in percent based on the system load average and the number of processors.
     *
     * @return The average CPU usage in percent
     */
    private double averageCpuUsage() {
        int processors = Runtime.getRuntime().availableProcessors();
        double loadAverage = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
        return loadAverage * 100 / processors;
    }
}