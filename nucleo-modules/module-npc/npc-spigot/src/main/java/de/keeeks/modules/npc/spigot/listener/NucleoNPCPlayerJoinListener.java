package de.keeeks.modules.npc.spigot.listener;

import de.keeeks.modules.npc.api.NPC;
import de.keeeks.modules.npc.api.NPCApi;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NucleoNPCPlayerJoinListener implements Listener {
    private final NPCApi npcApi = ServiceRegistry.service(NPCApi.class);

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Scheduler.runAsync(() -> {
            for (NPC npc : npcApi.npcs()) {
                npc.spawnForPlayer(player);
            }
        });
    }
}