package de.keeeks.nucleo.modules.moderation.tools.spigot.cps.clickcheck;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckInformation;
import de.keeeks.nucleo.modules.moderation.tools.spigot.cps.ClicksPerSecondInformation;
import de.keeeks.nucleo.modules.moderation.tools.spigot.cps.ClicksPerSecondProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClickCheckActionBarRunnable implements Runnable {
    private final ClicksPerSecondProvider clicksPerSecondProvider = ServiceRegistry.service(ClicksPerSecondProvider.class);
    private final ClickCheckApi clickCheckApi = ServiceRegistry.service(ClickCheckApi.class);

    @Override
    public void run() {
        for (ClickCheckInformation clickCheck : clickCheckApi.clickChecks()) {
            Player player = Bukkit.getPlayer(clickCheck.viewer());
            if (player == null) continue;
            Player target = Bukkit.getPlayer(clickCheck.target());
            if (target == null) continue;

            clicksPerSecondProvider.clicksPerSecondInformation(target).map(
                    ClicksPerSecondInformation::clicksPerSecond
            ).ifPresentOrElse(cps -> player.sendActionBar(Component.translatable(
                    "nucleo.modules.moderation.tools.cps.clickcheck.actionbar",
                    Component.text(cps)
            )), () -> player.sendActionBar(Component.translatable(
                    "nucleo.modules.moderation.tools.cps.clickcheck.actionbar.notMeasured"
            )));
        }
    }
}