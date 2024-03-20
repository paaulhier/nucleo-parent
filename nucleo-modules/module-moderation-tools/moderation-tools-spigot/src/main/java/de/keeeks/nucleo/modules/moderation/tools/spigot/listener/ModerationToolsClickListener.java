package de.keeeks.nucleo.modules.moderation.tools.spigot.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.moderation.tools.spigot.cps.ClicksPerSecondInformation;
import de.keeeks.nucleo.modules.moderation.tools.spigot.cps.ClicksPerSecondProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class ModerationToolsClickListener implements Listener {
    private final ClicksPerSecondProvider clicksPerSecondProvider = ServiceRegistry.service(
            ClicksPerSecondProvider.class
    );

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        clicksPerSecondProvider.handleClick(
                event.getPlayer(),
                clickType(action)
        );
    }

    @NotNull
    private ClicksPerSecondInformation.ClickType clickType(Action action) {
        return action.isLeftClick()
                ? ClicksPerSecondInformation.ClickType.LEFT
                : ClicksPerSecondInformation.ClickType.RIGHT;
    }
}