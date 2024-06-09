package de.keeeks.modules.npc.spigot;

import de.keeeks.modules.npc.api.NPC;
import de.keeeks.modules.npc.api.NPCApi;
import de.keeeks.modules.npc.spigot.listener.NucleoNPCPlayerJoinListener;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;

@ModuleDescription(
        name = "npc"
)
public class NPCSpigotModule extends SpigotModule {

    private NPCApi npcApi;

    @Override
    public void load() {
        ServiceRegistry.registerService(
                NPCApi.class,
                new NucleoNPCApi()
        );
    }

    @Override
    public void enable() {
        registerListener(new NucleoNPCPlayerJoinListener());
    }

    @Override
    public void disable() {
        for (NPC npc : npcApi.npcs()) {
            npc.remove();
        }
    }
}