package de.keeeks.modules.npc.api;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public interface NPCApi {

    /**
     * Returns all NPCs that are currently spawned
     *
     * @return all NPCs that are currently spawned
     */
    List<NPC> npcs();

    /**
     * Returns the nearest NPC to the given location
     *
     * @param location the location to search the nearest NPC for
     * @return the nearest NPC to the given location
     */
    default Optional<NPC> nearestNpc(Location location) {
        return npcs().stream().min(Comparator.comparingDouble(
                o -> o.location().distance(location)
        ));
    }

    /**
     * Creates a new NPC with the given skin model, location and name
     *
     * @param skinModel the skin model of the NPC
     * @param location  the location of the NPC
     * @param name      the name of the NPC
     * @return the created NPC instance
     */
    NPC createNpc(NPCSkinModel skinModel, Location location, Component name);

}