package de.keeeks.nucleo.modules.vanish.velocity.listener;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.modules.vanish.api.VanishApi;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NucleoVanishPlayerDisconnectListener {
    private final VanishApi vanishApi;

    @Subscribe
    public EventTask handleDisconnect(DisconnectEvent event) {
        return EventTask.async(() -> {
            Player player = event.getPlayer();

            vanishApi.invalidate(player.getUniqueId());
        });
    }
}