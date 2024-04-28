package de.keeeks.nucleo.modules.players.spigot.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.spigot.afk.AFKService;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class AFKPlayerListener implements Listener {
    private final AFKService afkService = ServiceRegistry.service(AFKService.class);

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleQuit(PlayerQuitEvent event) {
        afkService.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleJoin(PlayerJoinEvent event) {
        afkService.updateActivity(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleChat(AsyncChatEvent event) {
        afkService.updateActivity(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleMove(PlayerMoveEvent event) {
        afkService.updateActivity(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleInteract(PlayerInteractEvent event) {
        afkService.updateActivity(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleCommandSend(PlayerCommandSendEvent event) {
        afkService.updateActivity(event.getPlayer().getUniqueId());
    }
}