package de.keeeks.nucleo.modules.moderation.tools.spigot.cps.clickcheck;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckInformation;
import de.keeeks.nucleo.modules.moderation.tools.spigot.cps.ClicksPerSecondProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static de.keeeks.nucleo.modules.moderation.tools.spigot.cps.ClicksPerSecondInformation.ClickType;

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

            clicksPerSecondProvider.clicksPerSecondInformation(target).ifPresentOrElse(clicksPerSecondInformation -> {
                int leftClicks = clicksPerSecondInformation.clicksPerSecondByType(ClickType.LEFT);
                int rightClicks = clicksPerSecondInformation.clicksPerSecondByType(ClickType.RIGHT);
                player.sendActionBar(Component.translatable(
                        "nucleo.modules.moderation.tools.cps.clickcheck.actionbar",
                        Component.text(leftClicks),
                        Component.text(rightClicks),
                        Component.text(target.getName())
                ));
            }, () -> player.sendActionBar(Component.translatable(
                    "nucleo.modules.moderation.tools.cps.clickcheck.actionbar.notMeasured",
                    Component.text(target.getName())
            )));
        }
    }
}