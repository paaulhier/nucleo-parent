package de.keeeks.nucleo.modules.hologram.spigot.holograms;

import de.keeeks.nucleo.modules.hologram.api.Hologram;
import de.keeeks.nucleo.modules.hologram.api.HologramApi;
import org.bukkit.Location;

import java.util.LinkedList;
import java.util.List;

public class DefaultHologramApi implements HologramApi {
    private static final List<Hologram> holograms = new LinkedList<>();

    @Override
    public Hologram createHologram(String name, Location location) {
        DefaultHologram hologram = new DefaultHologram(
                name,
                location
        );

        holograms.add(hologram);

        return hologram;
    }

    @Override
    public List<Hologram> holograms() {
        return holograms;
    }

    @Override
    public Hologram hologram(String name) {
        return holograms.stream()
                .filter(hologram -> hologram.name().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removeHologram(String name) {
        holograms.removeIf(hologram -> hologram.name().equals(name));
    }

    @Override
    public void removeHologram(Hologram hologram) {
        holograms.remove(hologram);
    }

    @Override
    public void removeAllHolograms() {
        for (Hologram hologram : holograms) {
            hologram.remove();
            holograms.remove(hologram);
        }
    }

    @Override
    public void updateHologram(String name) {
        Hologram hologram = hologram(name);
        if (hologram != null) {
            hologram.update();
        }
    }
}