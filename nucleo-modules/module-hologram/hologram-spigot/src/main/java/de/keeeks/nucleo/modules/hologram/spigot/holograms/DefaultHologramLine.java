package de.keeeks.nucleo.modules.hologram.spigot.holograms;

import de.keeeks.nucleo.core.spigot.NucleoSpigotPlugin;
import de.keeeks.nucleo.modules.hologram.api.Hologram;
import de.keeeks.nucleo.modules.hologram.api.HologramLine;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.concurrent.atomic.AtomicReference;

@Getter
@Accessors(fluent = true)
public abstract class DefaultHologramLine<C> implements HologramLine<C> {
    private final AtomicReference<ArmorStand> armorStand = new AtomicReference<>();

    private final Hologram hologram;

    private C content;

    protected DefaultHologramLine(Hologram hologram, C content) {
        this.hologram = hologram;
        this.content = content;
        spawnArmorStand();
    }

    @Override
    public void content(C newContent) {
        this.content = newContent;
        spawnArmorStand();
    }

    @Override
    public void update() {
        spawnArmorStand();
    }

    @Override
    public void remove() {
        removeArmorStand();
    }

    protected void spawnArmorStand() {
        removeArmorStand();

        Location location = hologram.location().clone().subtract(
                0.0,
                hologram.yOffset(this),
                0.0
        );
        World world = location.getWorld();
        ArmorStand armorStand = world.spawn(
                location,
                ArmorStand.class
        );

        if (content instanceof Component component) {
            armorStand.customName(component);
            armorStand.setCustomNameVisible(true);
        } else if (content instanceof ItemStack itemStack) {
            armorStand.setCustomNameVisible(false);
            world.dropItem(
                    location,
                    itemStack,
                    item -> {
                        item.setPickupDelay(Integer.MAX_VALUE);
                        item.setMetadata(
                                "hologram",
                                new FixedMetadataValue(
                                        NucleoSpigotPlugin.plugin(),
                                        hologram.uuid()
                                )
                        );
                        item.setInvulnerable(true);
                        armorStand.addPassenger(item);
                    }
            );
        }

        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setSmall(true);
        armorStand.setMarker(true);
        armorStand.setInvulnerable(true);
        this.armorStand.set(armorStand);
    }

    protected void removeArmorStand() {
        ArmorStand armorStand = this.armorStand.get();
        if (armorStand != null) {
            armorStand.getPassengers().forEach(Entity::remove);
            armorStand.remove();
        }
    }
}