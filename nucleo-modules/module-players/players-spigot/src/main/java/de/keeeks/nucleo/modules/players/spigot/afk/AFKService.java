package de.keeeks.nucleo.modules.players.spigot.afk;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.OnlineState;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.spigot.SpigotPlayersModule;
import de.keeeks.nucleo.modules.players.spigot.afk.configuration.AFKConfiguration;
import de.keeeks.nucleo.modules.players.spigot.afk.configuration.TitleMessage;
import de.keeeks.nucleo.modules.players.spigot.event.PlayerChangeAwayStateEvent;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static net.kyori.adventure.text.Component.translatable;

public class AFKService {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final Map<UUID, Long> lastActivity = new HashMap<>();

    private final AFKConfiguration afkConfiguration = JsonConfiguration.create(
            Module.module(SpigotPlayersModule.class).dataFolder(),
            "afk"
    ).loadObject(AFKConfiguration.class, AFKConfiguration.createDefault());
    private final AtomicInteger titleIndex = new AtomicInteger(0);

    private final SpigotModule module;

    public AFKService(SpigotPlayersModule module) {
        this.module = module;
        if (!afkConfiguration.enabled()) return;
        Scheduler.runAsyncTimer(
                this::incrementTitleIndex,
                afkConfiguration.titleChangeInterval(),
                afkConfiguration.titleChangeIntervalUnit()
        );
        Bukkit.getScheduler().runTaskTimerAsynchronously(
                module.plugin(),
                () -> {
                    for (UUID uuid : Map.copyOf(lastActivity).keySet()) {
                        Player player = Bukkit.getPlayer(uuid);
                        if (player == null) {
                            remove(uuid);
                            continue;
                        }
                        if (isAFK(uuid) && afkConfiguration.showTitle()) {
                            TitleMessage titleMessage = activeTitleMessage();
                            player.showTitle(Title.title(
                                    translatable(titleMessage.title()),
                                    translatable(titleMessage.subtitle()),
                                    Title.Times.times(
                                            Duration.ZERO,
                                            Duration.ofSeconds(5),
                                            Duration.ZERO
                                    )
                            ));
                            if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) continue;
                            addPotionEffect(player);
                        }
                        playerService.onlinePlayer(uuid).ifPresent(onlinePlayer -> {
                            if (onlinePlayer.onlineState() != OnlineState.AWAY && isAFK(uuid)) {
                                changeState(uuid, onlinePlayer, OnlineState.AWAY);
                                return;
                            }
                            if (onlinePlayer.onlineState() == OnlineState.AWAY && !isAFK(uuid)) {
                                changeState(uuid, onlinePlayer, OnlineState.ONLINE);
                                clearAllForPlayer(player);
                            }
                        });
                    }
                },
                5,
                5
        );
    }

    private void incrementTitleIndex() {
        if (titleIndex.incrementAndGet() >= afkConfiguration.titleMessages().size()) titleIndex.set(0);
    }

    private TitleMessage activeTitleMessage() {
        return afkConfiguration.titleMessages().get(titleIndex.get());
    }

    public void forceChangeState(UUID uuid, NucleoOnlinePlayer onlinePlayer, OnlineState state) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        if (state == OnlineState.AWAY) {
            lastActivity.put(uuid, Instant.now().minus(
                    Duration.of(afkConfiguration.afkTime(), afkConfiguration.afkTimeUnit().toChronoUnit())
            ).minus(Duration.ofSeconds(30)).toEpochMilli());
        } else {
            lastActivity.put(uuid, System.currentTimeMillis());
        }
        if (state == OnlineState.AWAY) {
            addPotionEffect(player);
        } else {
            clearAllForPlayer(player);
        }
        changeState(uuid, onlinePlayer, state);
    }

    private void clearAllForPlayer(Player player) {
        removePotionEffect(player);
        player.clearTitle();
    }

    private void removePotionEffect(Player player) {
        Bukkit.getScheduler().runTask(module.plugin(), () -> player.removePotionEffect(PotionEffectType.BLINDNESS));
    }

    private void addPotionEffect(Player player) {
        Bukkit.getScheduler().runTask(module.plugin(), () -> player.addPotionEffect(new PotionEffect(
                PotionEffectType.BLINDNESS,
                Integer.MAX_VALUE,
                0,
                false,
                false
        )));
    }

    public void changeState(UUID uuid, NucleoOnlinePlayer onlinePlayer, OnlineState state) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        PlayerChangeAwayStateEvent awayStateEvent = buildChangeAwayStateEvent(onlinePlayer, player, state);
        onlinePlayer.updateOnlineState(awayStateEvent.newState()).update();
        if (!afkConfiguration.sendMessage()) return;
        player.sendMessage(translatable(
                "afk.message",
                translatable(awayStateEvent.away() ? "afk.message.away" : "afk.message.online")
        ));
    }

    public void updateActivity(UUID player) {
        lastActivity.put(player, System.currentTimeMillis());
    }

    public boolean isAFK(UUID player) {
        if (!lastActivity.containsKey(player)) return false;

        long afkTime = System.currentTimeMillis() - lastActivity.getOrDefault(player, 0L);
        return afkTime > afkConfiguration.afkTimeUnit().toMillis(afkConfiguration.afkTime());
    }

    public void remove(UUID player) {
        lastActivity.remove(player);
    }

    private @NotNull PlayerChangeAwayStateEvent buildChangeAwayStateEvent(
            NucleoOnlinePlayer onlinePlayer,
            Player player,
            OnlineState newState
    ) {
        PlayerChangeAwayStateEvent awayStateEvent = new PlayerChangeAwayStateEvent(
                player,
                newState == OnlineState.AWAY,
                onlinePlayer.onlineState(),
                newState
        );
        Bukkit.getPluginManager().callEvent(awayStateEvent);
        return awayStateEvent;
    }
}