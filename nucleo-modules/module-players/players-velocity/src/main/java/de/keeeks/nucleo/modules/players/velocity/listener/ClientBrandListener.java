package de.keeeks.nucleo.modules.players.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerClientBrandEvent;
import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.ClientBrand;
import de.keeeks.nucleo.modules.players.api.PlayerService;

public final class ClientBrandListener {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);

    @Subscribe
    public void handleClientBrandInformation(PlayerClientBrandEvent event) {
        Player player = event.getPlayer();
        String brand = event.getBrand();

        playerService.onlinePlayer(player.getUniqueId()).ifPresent(
                onlinePlayer -> onlinePlayer.updateClientBrand(ClientBrand.fromString(brand)).update()
        );
    }
}