package de.keeeks.nucleo.modules.tablist.event;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static net.kyori.adventure.text.Component.*;

@Getter
@Setter
public class PlayerTabListForPlayerEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Map<Team.Option, Team.OptionStatus> options = new HashMap<>() {{
        put(Team.Option.COLLISION_RULE, Team.OptionStatus.ALWAYS);
        put(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
    }};
    private final TreeMap<String, Component> prefixes = new TreeMap<>();
    private final TreeMap<String, Component> suffixes = new TreeMap<>();

    private final TargetType targetType;
    private final Player viewer;

    private int priority = 0;
    private String teamNamePrefix = "nsb-";
    private boolean cancelled = false;
    private boolean canSeeFriendlyInvisibles = false;
    private boolean allowFriendlyFire = false;

    private NamedTextColor color = NamedTextColor.WHITE;

    public PlayerTabListForPlayerEvent(@NotNull Player who, Player viewer, TargetType targetType) {
        super(who, false);
        this.targetType = targetType;
        this.viewer = viewer;
    }

    public PlayerTabListForPlayerEvent option(Team.Option option, Team.OptionStatus status) {
        options.compute(option, (option1, optionStatus) -> status);
        return this;
    }

    public String buildTeamName() {
        return teamNamePrefix + priority;
    }

    public Component prefix() {
        if (prefixes.isEmpty()) return empty();
        return join(JoinConfiguration.separator(space().toBuilder().resetStyle()), prefixes.values());
    }

    public Component suffix() {
        if (suffixes.isEmpty()) return empty();
        return join(JoinConfiguration.separator(space().toBuilder().resetStyle()), suffixes.values());
    }

    public void prefix(String key, Component prefix) {
        prefixes.put(key, prefix);
    }

    public void suffix(String key, Component suffix) {
        suffixes.put(key, suffix);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public enum TargetType {
        TAB_LIST,
        NAME_TAG
    }
}