package de.keeeks.nucleo.modules.hologram.api;

import org.bukkit.Location;

import java.util.UUID;

public interface Hologram {

    /**
     * Returns the unique identifier of this hologram.
     * @return the unique identifier of this hologram
     */
    UUID uuid();

    /**
     * Returns the location of this hologram.
     * @return the location of this hologram
     */
    Location location();

    /**
     * Returns the name of this hologram.
     * @return the name of this hologram
     */
    String name();

    /**
     * Returns the visibility of this hologram.
     */
    void update();

    /**
     * Returns the visibility of this hologram.
     */
    void remove();

    /**
     * Moves this hologram to the given location.
     * @param location the new location of this hologram
     */
    void move(Location location);

    /**
     * Returns the y offset of the given hologram line.
     * @param hologramLine the hologram line
     * @return the y offset of the given hologram line
     * @param <C> the content type of the hologram line
     */
    <C> double yOffset(HologramLine<C> hologramLine);

    /**
     * Adds a new line to this hologram.
     * @param content the content of the new line
     * @return the new line
     * @param <C> the content type of the new line
     */
    <C> HologramLine<C> addLine(C content);

    /**
     * Adds a new line to this hologram at the given index.
     * @param index the index of the new line
     * @param content the content of the new line
     * @return the new line
     * @param <C> the content type of the new line
     */
    <C> HologramLine<C> addLine(int index, C content);

    /**
     * Returns the line at the given index.
     * @param index the index of the line
     * @return the line at the given index
     * @param <C> the content type of the line
     */
    <C> HologramLine<C> line(int index);

    /**
     * Returns the index of the given line.
     * @param index the index of the line
     */
    void removeLine(int index);

    /**
     * Removes the given line from this hologram.
     * @param line the line to remove
     * @param <C> the content type of the line
     */
    <C> void removeLine(HologramLine<C> line);

    /**
     * Removes all lines from this hologram.
     */
    void removeAllLines();

}