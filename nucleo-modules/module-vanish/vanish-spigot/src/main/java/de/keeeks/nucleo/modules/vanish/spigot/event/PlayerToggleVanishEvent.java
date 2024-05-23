package de.keeeks.nucleo.modules.vanish.spigot.event;

import de.keeeks.nucleo.modules.vanish.api.VanishData;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerToggleVanishEvent extends PlayerEvent {
    private static final HandlerList handlerList = new HandlerList();

    private final VanishData vanishData;
    private final boolean vanished;

    public PlayerToggleVanishEvent(@NotNull Player who, VanishData vanishData) {
        super(who, true);
        this.vanishData = vanishData;
        this.vanished = vanishData.vanished();
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}