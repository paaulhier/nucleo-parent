package de.keeeks.nucleo.core.spigot;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.loader.ModuleLoader;
import de.keeeks.nucleo.core.loader.classloader.ModuleClassLoader;
import de.keeeks.nucleo.core.spigot.json.LocationSerializer;
import de.keeeks.nucleo.core.spigot.listener.NucleoPluginMessageListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

@Getter
public class NucleoSpigotPlugin extends JavaPlugin {
    @Getter
    private static NucleoSpigotPlugin plugin;

    private final List<Object> commandRegistrations = new LinkedList<>();

    private final ModuleClassLoader moduleClassLoader = ModuleClassLoader.create(
            NucleoSpigotPlugin.class.getClassLoader()
    );
    private final ModuleLoader moduleLoader = ModuleLoader.create(
            getLogger()
    );


    @Getter
    private final long startupTime = System.currentTimeMillis();
    @Getter
    private Duration startupDuration;

    private BukkitCommandHandler bukkitCommandHandler;

    @Override
    public void onLoad() {
        plugin = this;

        GsonBuilder.registerSerializer(new LocationSerializer());

        ModuleLoader.classLoader(moduleClassLoader);
        moduleLoader.loadModulesFromFolder();
        moduleLoader.loadModules();
    }

    @Override
    public void onEnable() {
        bukkitCommandHandler = BukkitCommandHandler.create(this);
        bukkitCommandHandler.enableAdventure();
        bukkitCommandHandler.register(commandRegistrations.toArray());
        bukkitCommandHandler.registerBrigadier();

        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(
                this,
                "nucleo:main",
                new NucleoPluginMessageListener()
        );

        moduleLoader.enableModules();

        this.startupDuration = Duration.ofMillis(
                System.currentTimeMillis() - startupTime
        );

        Module.modules().forEach(Module::postStartup);
    }

    @Override
    public void onDisable() {
        Scheduler.shutdown();
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