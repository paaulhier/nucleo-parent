package de.keeeks.nucleo.core.proxy.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.proxy.NucleoProxyPlugin;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bungee.BungeeCommandActor;
import revxrsal.commands.bungee.annotation.CommandPermission;

import java.util.List;
import java.util.UUID;

@Command({"modules", "module"})
@CommandPermission("nucleo.commands.modules")
public class ModulesCommand {
    private final NucleoProxyPlugin plugin;

    public ModulesCommand(NucleoProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @DefaultFor({"modules", "module"})
    public void modulesCommand(
            final BungeeCommandActor actor
    ) {
        ProxiedPlayer player = actor.asPlayer();
        if (player == null) return;

        List<Module> modules = Module.modules();

        plugin.bungeeAudiences().player(player).sendMessage(Component.text("Modules (%s): ".formatted(
                modules.size()
        )).append(Module.modulesAsComponent()));
    }

    @Subcommand({"spigot", "bukkit"})
    public void modulesSpigotCommand(
            final BungeeCommandActor actor
    ) {
        ProxiedPlayer player = actor.asPlayer();
        if (player == null) return;

        player.getServer().getInfo().sendData(
                "nucleo:main",
                createDataOutput(
                        player.getUniqueId(),
                        "modules"
                ).toByteArray()
        );
    }

    private ByteArrayDataOutput createDataOutput(
            UUID uuid,
            String message
    ) {
        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
        byteArrayDataOutput.writeUTF("message");
        byteArrayDataOutput.writeLong(uuid.getMostSignificantBits());
        byteArrayDataOutput.writeLong(uuid.getLeastSignificantBits());
        byteArrayDataOutput.writeUTF(message);
        return byteArrayDataOutput;
    }
}