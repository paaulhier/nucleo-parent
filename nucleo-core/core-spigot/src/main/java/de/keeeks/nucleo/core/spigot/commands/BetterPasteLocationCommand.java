package de.keeeks.nucleo.core.spigot.commands;

import com.google.gson.Gson;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Switch;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.text.DecimalFormat;
import java.text.NumberFormat;

@Command({"betterpastelocation", "bpl"})
@CommandPermission("nucleo.command.betterpastelocation")
public class BetterPasteLocationCommand {
    private final NumberFormat locationFormat = new DecimalFormat("0.0#");
    private final Gson gson = GsonBuilder.globalGson();

    @DefaultFor({"betterpastelocation", "bpl"})
    public void pasteLocation(
            final BukkitCommandActor actor,
            @Switch(value = "correctPitch") boolean correctPitch,
            @Switch("correctYaw") boolean correctYaw
    ) {
        if (!actor.isPlayer()) return;
        Player player = actor.getAsPlayer();
        var location = player.getLocation();

        Component xComponent = formatLocation("X", location.getX());
        Component yComponent = formatLocation("Y", location.getY());
        Component zComponent = formatLocation("Z", location.getZ());
        float yaw = correctYaw
                ? correctYaw(location.getYaw())
                : location.getYaw();
        Component yawComponent = formatLocation("Yaw", yaw);
        float pitch = correctPitch
                ? roundPitch(location.getPitch())
                : location.getPitch();
        Component pitchComponent = formatLocation("Pitch", pitch);

        teleportPlayerIfNecessary(
                correctPitch,
                correctYaw,
                location,
                player,
                pitch,
                yaw
        );

        Component locationComponent = Component.join(
                JoinConfiguration.newlines(),
                xComponent,
                yComponent,
                zComponent,
                yawComponent,
                pitchComponent
        ).clickEvent(
                ClickEvent.copyToClipboard(gson.toJson(location))
        ).hoverEvent(HoverEvent.showText(
                Component.text("Click to copy location to clipboard", NamedTextColor.GRAY)
        ));

        actor.audience().sendMessage(locationComponent);
    }

    private static void teleportPlayerIfNecessary(
            boolean correctPitch,
            boolean correctYaw,
            Location location,
            Player player,
            float pitch,
            float yaw
    ) {
        Location correctedLocation = location.clone();
        if (correctPitch) {
            correctedLocation.setPitch(pitch);
        }
        if (correctYaw) {
            correctedLocation.setYaw(yaw);
        }
        if (correctPitch || correctYaw) {
            player.teleportAsync(correctedLocation);
        }
    }

    private float roundPitch(double pitch) {
        if (pitch <= -45) {
            return -90;
        }
        if (pitch <= 0) {
            return 0;
        }
        if (pitch <= 45) {
            return 0;
        }
        return 90;
    }

    private float correctYaw(double yaw) {
        if (yaw < -135) {
            return -180;
        }
        if (yaw < -45) {
            return -90;
        }
        if (yaw < 45) {
            return 0;
        }
        if (yaw < 135) {
            return 90;
        }
        return 180;
    }

    private Component formatLocation(String name, double location) {
        return Component.text(name + ": ", NamedTextColor.GRAY).append(
                Component.text(locationFormat.format(location), NamedTextColor.YELLOW)
        );
    }
}