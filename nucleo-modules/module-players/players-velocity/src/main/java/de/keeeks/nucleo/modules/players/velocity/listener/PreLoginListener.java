package de.keeeks.nucleo.modules.players.velocity.listener;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;

import java.util.UUID;

public class PreLoginListener {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);

    @Subscribe
    public EventTask handlePreLogin(PreLoginEvent event) {
        UUID uniqueId = event.getUniqueId();
        return EventTask.async(() -> {
            if (playerService.onlinePlayer(uniqueId).isPresent()) {
                event.setResult(PreLoginEvent.PreLoginComponentResult.denied(
                        Component.text("You are already logged in!", Style.style(NamedTextColor.RED))
                ));
            }
        });
    }
}