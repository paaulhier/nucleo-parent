package de.keeeks.modules.npc.api;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public interface NPCApi {

    List<NPC> npcs();

    default Optional<NPC> nearestNpc(Location location) {
        return npcs().stream().min(Comparator.comparingDouble(
                o -> o.location().distance(location)
        ));
    }

    default Optional<NPC> npc(String name) {
        return npcs().stream().filter(
                npc -> npc.displayName().equals(name)
        ).findFirst();
    }

    NPC createNpc(NPCSkinModel skinModel, Location location, Component name);

}