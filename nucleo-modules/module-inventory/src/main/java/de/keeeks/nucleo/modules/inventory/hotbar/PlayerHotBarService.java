package de.keeeks.nucleo.modules.inventory.hotbar;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.inventory.InventoryModule;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerHotBarService {
    protected final InventoryModule module = Module.module(InventoryModule.class);
    protected final List<PlayerHotBar> hotBars = new ArrayList<>();

    @Setter
    private HotBarCreator hotBarCreator = NucleoPlayerHotBar::new;

    public PlayerHotBar createPlayerHotBar(Player player) {
        PlayerHotBar playerHotBar = hotBarCreator.create(player);
        hotBars.add(playerHotBar);
        updateMetadata(player, playerHotBar);
        return playerHotBar;
    }

    protected final void updateMetadata(Player player, PlayerHotBar hotBar) {
        player.setMetadata(
                "hotBarId",
                new FixedMetadataValue(module.plugin(), hotBar.id().toString())
        );
    }

    public final void deleteHotBar(PlayerHotBar hotBar) {
        hotBar.clear();
        hotBars.remove(hotBar);
    }

    public final void deleteHotBar(Player owner) {
        PlayerHotBar hotBar = hotBar(owner);
        if (hotBar == null) return;
        deleteHotBar(hotBar);
    }

    public final PlayerHotBar hotBar(Player player) {
        MetadataValue hotBarIdMetadata = player.getMetadata("hotBarId").stream().findFirst().orElse(null);

        if (hotBarIdMetadata == null) {
            return createPlayerHotBar(player);
        }
        UUID hotBarId = UUID.fromString(hotBarIdMetadata.asString());

        return hotBars.stream().filter(
                h -> h.id().equals(hotBarId)
        ).findFirst().orElse(createPlayerHotBar(player));
    }
}