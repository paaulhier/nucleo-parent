package de.keeeks.nucleo.core.spigot;

import de.keeeks.nucleo.core.loader.ModuleLoader;
import de.keeeks.nucleo.core.loader.classloader.ModuleClassLoader;
import de.keeeks.nucleo.core.spigot.commands.ModulesCommand;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.util.LinkedList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class NucleoSpigotPlugin extends JavaPlugin {
    @Getter
    @Accessors(fluent = true)
    private static NucleoSpigotPlugin plugin;

    private final List<Object> commandRegistrations = new LinkedList<>();

    private final ModuleClassLoader moduleClassLoader = ModuleClassLoader.create(
            NucleoSpigotPlugin.class.getClassLoader()
    );
    private final ModuleLoader moduleLoader = ModuleLoader.create(
            getLogger()
    );
    private BukkitAudiences bukkitAudiences;

    private BukkitCommandHandler bukkitCommandHandler;
    @Override
    public void onLoad() {
        plugin = this;

        ModuleLoader.classLoader(moduleClassLoader);
        moduleLoader.loadModulesFromFolder();
        moduleLoader.loadModules();
    }

    @Override
    public void onEnable() {
        bukkitAudiences = BukkitAudiences.create(this);

        bukkitCommandHandler = BukkitCommandHandler.create(this);
        bukkitCommandHandler.enableAdventure(bukkitAudiences);
        bukkitCommandHandler.register(new ModulesCommand(this));
        bukkitCommandHandler.register(commandRegistrations.toArray());
        bukkitCommandHandler.registerBrigadier();

        moduleLoader.enableModules();
    }

    @Override
    public void onDisable() {
        moduleLoader.disableModules();
    }

    public void registerCommands(Object... commands) {
        if (!isEnabled()) {
            commandRegistrations.addAll(List.of(commands));
            getLogger().info("Put commands in queue to register later");
            return;
        }
        bukkitCommandHandler.register(commands);
    }
    public void registerListener(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}