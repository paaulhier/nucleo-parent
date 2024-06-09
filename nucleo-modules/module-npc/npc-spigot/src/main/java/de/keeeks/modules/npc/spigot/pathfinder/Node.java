package de.keeeks.modules.npc.spigot.pathfinder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@Setter
@AllArgsConstructor
public class Node {
    private Location location;
    private Node parent;
    private double gCost;
    private double hCost;
    private double fCost;

}