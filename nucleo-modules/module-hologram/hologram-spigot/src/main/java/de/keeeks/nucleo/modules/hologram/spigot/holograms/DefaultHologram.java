package de.keeeks.nucleo.modules.hologram.spigot.holograms;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.hologram.api.Hologram;
import de.keeeks.nucleo.modules.hologram.api.HologramApi;
import de.keeeks.nucleo.modules.hologram.api.HologramLine;
import de.keeeks.nucleo.modules.hologram.api.TextHologramLine;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
public class DefaultHologram implements Hologram {
    private static final HologramApi hologramApi = ServiceRegistry.service(HologramApi.class);

    private final List<HologramLine> hologramLines = new LinkedList<>();

    private final UUID uuid = UUID.randomUUID();

    private final String name;
    private final Location location;

    public DefaultHologram(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void update() {
        for (HologramLine hologramLine : hologramLines) {
            hologramLine.update();
        }
    }

    @Override
    public void remove() {
        hologramApi.removeHologramFromCache(this);
        for (HologramLine hologramLine : List.copyOf(hologramLines)) {
            hologramLine.remove();
            hologramLines.remove(hologramLine);
        }
    }

    /**
     * Moves this hologram to the given location.
     *
     * @param location the new location of this hologram
     */
    @Override
    public void move(Location location) {
        this.location.setX(location.getX());
        this.location.setY(location.getY());
        this.location.setZ(location.getZ());
        this.location.setWorld(location.getWorld());
        update();
    }

    @Override
    public <C> double yOffset(HologramLine<C> hologramLine) {
        double yOffset = 0.0;

        for (HologramLine line : hologramLines) {
            if (line instanceof TextHologramLine) {
                yOffset += 0.25;
            } else {
                yOffset += 0.45;
            }

            if (line == hologramLine) {
                break;
            }
        }
        return yOffset;
    }

    @Override
    public <C> HologramLine<C> addLine(C content) {
        if (content instanceof Component component) {
            HologramLine<Component> line = createComponentLine(component);
            hologramLines.add(line);
            return (HologramLine<C>) line;
        } else if (content instanceof ItemStack itemStack) {
            HologramLine<ItemStack> line = createItemLine(itemStack);
            hologramLines.add(line);
            return (HologramLine<C>) line;
        } else {
            throw new IllegalArgumentException(
                    "Unsupported content type: " + content.getClass().getName()
            );
        }
    }

    private HologramLine<Component> createComponentLine(Component content) {
        return new DefaultTextHologramLine(
                this,
                content
        );
    }

    private HologramLine<ItemStack> createItemLine(ItemStack content) {
        return new DefaultItemHologramLine(
                this,
                content
        );
    }

    @Override
    public <C> HologramLine<C> addLine(int index, C content) {
        if (content instanceof Component component) {
            HologramLine<Component> line = createComponentLine(component);
            hologramLines.add(index, line);
            return (HologramLine<C>) line;
        } else if (content instanceof ItemStack itemStack) {
            HologramLine<ItemStack> line = createItemLine(itemStack);
            hologramLines.add(index, line);
            return (HologramLine<C>) line;
        } else {
            throw new IllegalArgumentException(
                    "Unsupported content type: " + content.getClass().getName()
            );
        }
    }

    @Override
    public <C> HologramLine<C> line(int index) {
        return (HologramLine<C>) hologramLines.get(index);
    }

    @Override
    public void removeLine(int index) {
        HologramLine<?> line = hologramLines.get(index);
        line.remove();
        hologramLines.remove(index);
    }

    @Override
    public <C> void removeLine(HologramLine<C> line) {
        line.remove();
        hologramLines.remove(line);
    }

    @Override
    public void removeAllLines() {
        for (HologramLine<?> hologramLine : hologramLines) {
            hologramLine.remove();
            hologramLines.remove(hologramLine);
        }
    }
}