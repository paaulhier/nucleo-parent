package de.keeeks.modules.npc.api;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface NPC {

    /**
     * Returns the unique id of the NPC
     * @return the unique id of the NPC
     */
    UUID uniqueId();

    /**
     * Returns the entity id of the NPC
     * @return the entity id of the NPC
     */
    int entityId();

    /**
     * Returns the name of the NPC
     * @return the name of the NPC
     */
    Component displayName();

    /**
     * Returns the location of the NPC
     * @return the location of the NPC
     */
    Location location();

    /**
     * Spawns the NPC at the initial location
     */
    void spawn();

    /**
     * Spawns the NPC for the given player
     * @param player the player to spawn the NPC for
     */
    void spawnForPlayer(Player player);

    /**
     * Removes the NPC from the world
     */
    void remove();

    /**
     * Removes the NPC from the world for the given player
     * @param player the player to remove the NPC for
     */
    void removeForPlayer(Player player);

    /**
     * Lets the NPC teleport to the given location
     *
     * @param location the location the npc should teleport to
     */
    void teleport(Location location);

    /**
     * Lets the NPC walk to the given location
     *
     * @param location the location the npc should walk to
     */
    default void move(Location location) {
        move(location, () -> {});
    }

    /**
     * Lets the NPC walk to the given location
     * @param location the location the npc should walk to
     * @param moveComplete the runnable that should be executed when the NPC has reached the location
     */
    void move(Location location, Runnable moveComplete);

    /**
     * Plays the given animation for the NPC
     * @param animation the animation to play
     */
    void playAnimation(Animation animation);

    default void hit() {
        playAnimation(Animation.SWING_MAIN_ARM);
    }

    default void takeDamage() {
        playAnimation(Animation.TAKE_DAMAGE);
    }

    /**
     * Sets the skin of the NPC
     * @param skinModel the skin model
     */
    void setSkin(NPCSkinModel skinModel);

}