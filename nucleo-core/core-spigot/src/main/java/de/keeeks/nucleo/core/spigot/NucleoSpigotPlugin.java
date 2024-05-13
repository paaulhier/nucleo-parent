package de.keeeks.nucleo.core.spigot;

import de.keeeks.nucleo.core.api.NucleoUncaughtExceptionHandler;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.core.api.logger.NucleoLogger;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.loader.ModuleLoader;
import de.keeeks.nucleo.core.loader.classloader.ModuleClassLoader;
import de.keeeks.nucleo.core.spigot.command.NucleoSpigotExceptionHandler;
import de.keeeks.nucleo.core.spigot.json.LocationSerializer;
import de.keeeks.nucleo.core.spigot.json.WorldSerializer;
import de.keeeks.nucleo.core.spigot.listener.NucleoPluginMessageListener;
import de.keeeks.nucleo.core.spigot.listener.PlayerCommandExecutionListener;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.service.ServiceInfoSnapshot;
import eu.cloudnetservice.wrapper.holder.ServiceInfoHolder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.CommandHandlerVisitor;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Getter
public class NucleoSpigotPlugin extends JavaPlugin {
    @Getter
    private static NucleoSpigotPlugin plugin;

    private final AtomicReference<String> templateName = new AtomicReference<>();
    private final AtomicReference<String> serverName = new AtomicReference<>();
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
        NucleoLogger.logger(NucleoLogger.create(getLogger()));
        Thread.setDefaultUncaughtExceptionHandler(new NucleoUncaughtExceptionHandler());

        GsonBuilder.registerSerializer(
                new LocationSerializer(),
                new WorldSerializer()
        );

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
        bukkitCommandHandler.setExceptionHandler(new NucleoSpigotExceptionHandler(getLogger()));

        registerListener(new PlayerCommandExecutionListener(getLogger()));

        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(
                this,
                "nucleo:main",
                new NucleoPluginMessageListener()
        );

        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.getPlugin("CloudNet-Bridge") != null) {
            ServiceInfoSnapshot serviceInfoSnapshot = InjectionLayer.ext().instance(
                    ServiceInfoHolder.class
            ).serviceInfo();
            templateName.set(serviceInfoSnapshot.serviceId().taskName());
            serverName.set(serviceInfoSnapshot.name());
        } else {
            try {
                readServiceInfo(
                        this.serverName::set,
                        this.templateName::set,
                        () -> disablePluginDueToNoServerData(pluginManager)
                );
            } catch (Throwable throwable) {
                disablePluginDueToNoServerData(pluginManager);
            }
        }

        moduleLoader.enableModules();
        moduleLoader.postStartupModules();

        this.startupDuration = Duration.ofMillis(
                System.currentTimeMillis() - startupTime
        );
    }

    private void disablePluginDueToNoServerData(PluginManager pluginManager) {
        getLogger().info("No CloudNet-Bridge plugin found and no server.properties file found," +
                " disabling nucleo.");
        pluginManager.disablePlugin(this);
    }

    private void readServiceInfo(
            Consumer<String> serverNameConsumer,
            Consumer<String> templateNameConsumer,
            Runnable noDataPresent
    ) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("server.properties"));
        String serverName = properties.getProperty("server-name");
        String templateName = properties.getProperty("template-name");
        if (serverName == null && templateName == null) {
            noDataPresent.run();
            return;
        }
        if (serverName != null) {
            serverNameConsumer.accept(serverName);
        }
        if (templateName != null) {
            templateNameConsumer.accept(templateName);
        }
    }

    @Override
    public void onDisable() {
        Scheduler.shutdown();
        moduleLoader.disableModules();
    }

    public Optional<String> serverName() {
        return Optional.ofNullable(serverName.get());
    }

    public Optional<String> templateName() {
        return Optional.ofNullable(templateName.get());
    }

    public Optional<String> templateOrServerName() {
        return templateName().or(this::serverName);
    }

    public void registerCommands(Object... commands) {
        if (!isEnabled()) {
            commandRegistrations.addAll(List.of(commands));
            getLogger().info("Put commands in queue to register later");
            return;
        }
        bukkitCommandHandler.register(commands);

        Arrays.stream(commands).filter(
                o -> o instanceof CommandHandlerVisitor
        ).map(
                o -> ((CommandHandlerVisitor) o)
        ).forEach(commandHandlerVisitor -> commandHandlerVisitor.visit(bukkitCommandHandler));
    }

    public void registerListener(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}