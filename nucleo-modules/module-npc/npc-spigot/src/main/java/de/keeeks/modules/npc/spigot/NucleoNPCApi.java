package de.keeeks.modules.npc.spigot;

import de.keeeks.modules.npc.api.NPC;
import de.keeeks.modules.npc.api.NPCApi;
import de.keeeks.modules.npc.api.NPCSkinModel;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class NucleoNPCApi implements NPCApi {
    private final List<NPC> npcs = new ArrayList<>();

    @Override
    public List<NPC> npcs() {
        return List.copyOf(npcs);
    }

    @Override
    public NPC createNpc(NPCSkinModel skinModel, Location location, Component name) {
        NucleoNPC nucleoNPC = new NucleoNPC(
                skinModel,
                name,
                location
        );
        npcs.add(nucleoNPC);
        return nucleoNPC;
    }
}