package de.keeeks.nucleo.modules.hologram.api;

import org.bukkit.Location;

import java.util.UUID;

public interface Hologram {

    UUID uuid();

    Location location();

    String name();

    void update();

    void remove();

    <C> double yOffset(HologramLine<C> hologramLine);

    <C> HologramLine<C> addLine(C content);

    <C> HologramLine<C> addLine(int index, C content);

    <C> HologramLine<C> line(int index);

    void removeLine(int index);

    <C> void removeLine(HologramLine<C> line);

    void removeAllLines();

}