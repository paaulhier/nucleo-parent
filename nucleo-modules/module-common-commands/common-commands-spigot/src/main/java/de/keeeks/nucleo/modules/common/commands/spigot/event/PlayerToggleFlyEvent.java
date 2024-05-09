package de.keeeks.nucleo.modules.common.commands.spigot.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerToggleFlyEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Player executor;
    private final boolean flying;

    @Getter
    @Setter
    private boolean cancelled = true;


    public PlayerToggleFlyEvent(@NotNull Player who, Player executor, boolean flying) {
        super(who);
        this.executor = executor;
        this.flying = flying;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}