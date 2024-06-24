package de.keeeks.modules.npc.spigot.pathfinder;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Fence;

import java.util.*;

public class NPCPathfinder {

    public List<Location> findPath(Location start, Location end) {
        List<Node> openList = new ArrayList<>();
        List<Node> closedList = new ArrayList<>();

        Node startNode = new Node(start, null, 0, start.distance(end), start.distance(end));
        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node currentNode = openList.stream().min(Comparator.comparingDouble(Node::fCost)).orElseThrow();

            if (sameLocation(currentNode.location(), end)) {
                List<Location> path = new ArrayList<>();
                while (currentNode != null) {
                    path.add(currentNode.location());
                    currentNode = currentNode.parent();
                }
                Collections.reverse(path);
                return path;
            }

            openList.remove(currentNode);
            closedList.add(currentNode);

            for (Location neighbor : getNeighbors(currentNode.location())) {
                Node neighborNode = new Node(
                        neighbor,
                        currentNode,
                        currentNode.gCost() + 1,
                        neighbor.distance(end),
                        currentNode.gCost() + 1 + neighbor.distance(end)
                );

                if (closedList.stream().anyMatch(n -> sameLocation(n.location(), neighbor)) || !isWalkable(neighbor)) {
                    continue;
                }

                Optional<Node> openNode = openList.stream().filter(n -> sameLocation(n.location(), neighbor)).findFirst();
                if (openNode.isPresent()) {
                    if (openNode.get().gCost() > neighborNode.gCost()) {
                        openNode.get().parent(currentNode);
                        openNode.get().gCost(neighborNode.gCost());
                        openNode.get().fCost(neighborNode.fCost());
                    }
                } else {
                    openList.add(neighborNode);
                }
            }
        }

        return null;
    }

    private List<Location> getNeighbors(Location location) {
        List<Location> neighbors = new ArrayList<>();

        for (double x = -1; x <= 1; x += 0.5) {
            for (double y = -1; y <= 1; y += 0.5) {
                for (double z = -1; z <= 1; z += 0.5) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip the current location itself
                    Location neighbor = location.clone().add(x, y, z);
                    neighbors.add(neighbor);
                }
            }
        }

        return neighbors;
    }

    private boolean isWalkable(Location location) {
        Block block = location.getBlock();

        if (block.getBlockData() instanceof Fence) {
            return false;
        }

        Block below = block.getRelative(BlockFace.DOWN);

        if (below.getType().isAir() || below.isPassable() || !below.getType().isSolid()) {
            return false;
        }
        Block above = block.getRelative(BlockFace.UP);
        int stepUpCount = 0;

        while (!above.getType().isAir()) {
            above = above.getRelative(BlockFace.UP);
            stepUpCount++;
        }

        if (stepUpCount > 1 || block.getBlockData() instanceof Fence) {
            return false;
        }
        return isBlockWalkable(block);
    }

    private boolean isBlockWalkable(Block block) {
        if (block.getType().isAir() || block.isPassable()) {
            return true;
        }

        return !block.getType().isSolid();
    }

    private boolean sameLocation(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX()
                && loc1.getBlockY() == loc2.getBlockY()
                && loc1.getBlockZ() == loc2.getBlockZ();
    }
}