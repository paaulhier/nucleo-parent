package de.keeeks.nucleo.modules.vanish.spigot.event;

import de.keeeks.nucleo.modules.vanish.api.VanishData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
@Getter
public class PlayerVanishForPlayerEvent extends PlayerEvent {
    private static final HandlerList handlerList = new HandlerList();

    private final VanishData vanishData;
    private final Player viewer;

    @Setter
    private boolean cancelled = false;

    public PlayerVanishForPlayerEvent(@NotNull Player who, Player viewer, VanishData vanishData) {
        super(who, true);
        this.vanishData = vanishData;
        this.viewer = viewer;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}