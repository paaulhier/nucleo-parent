package de.keeeks.nucleo.modules.privacy.spigot.inventory.item;

import de.keeeks.karistus.api.PunishmentApi;
import de.keeeks.karistus.api.PunishmentType;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.privacy.api.PrivacyApi;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AsyncItem;

import static net.kyori.adventure.text.Component.translatable;

public class DeclinePrivacyItem extends AsyncItem {
    private static final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private static final PunishmentApi punishmentApi = ServiceRegistry.service(PunishmentApi.class);
    private static final PrivacyApi privacyApi = ServiceRegistry.service(PrivacyApi.class);

    public DeclinePrivacyItem() {
        super(new ItemBuilder(Material.CLOCK).setDisplayName(new AdventureComponentWrapper(
                translatable("inventory.privacy.loading")
        )), () -> new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(
                new AdventureComponentWrapper(
                        translatable("inventory.privacy.decline")
                )
        ));
    }

    @Override
    public void handleClick(
            @NotNull ClickType clickType,
            @NotNull Player player,
            @NotNull InventoryClickEvent event
    ) {
        privacyApi.privacyInformation(player.getUniqueId()).ifPresent(
                privacyApi::decline
        );
        playerService.onlinePlayer(player.getUniqueId()).ifPresent(
                onlinePlayer -> onlinePlayer.kick(translatable("privacy.declined"), true)
        );
        punishmentApi.template(
                "privacy",
                PunishmentType.BAN
        ).ifPresent(punishmentTemplate -> punishmentApi.ban(
                player.getUniqueId(),
                PunishmentApi.consoleId,
                punishmentTemplate
        ));
    }
}