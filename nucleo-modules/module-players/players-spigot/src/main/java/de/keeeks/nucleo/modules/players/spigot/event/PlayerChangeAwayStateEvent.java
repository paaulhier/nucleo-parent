package de.keeeks.nucleo.modules.players.spigot.event;

import de.keeeks.nucleo.modules.players.api.OnlineState;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerChangeAwayStateEvent extends PlayerEvent {
    private static final HandlerList handlerList = new HandlerList()    ;

    private final boolean away;
    private final OnlineState oldState;
    private OnlineState newState;

    public PlayerChangeAwayStateEvent(
            @NotNull Player who,
            boolean away,
            OnlineState oldState,
            OnlineState newState
    ) {
        super(who, true);
        this.away = away;
        this.oldState = oldState;
        this.newState = newState;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}