package de.keeeks.nucleo.core.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.core.loader.ModuleLoader;
import de.keeeks.nucleo.core.loader.classloader.ModuleClassLoader;
import de.keeeks.nucleo.core.velocity.command.NucleoVelocityExceptionHandler;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.AutoCompleter;
import revxrsal.commands.velocity.VelocityCommandHandler;

import java.util.logging.Logger;

@Getter
@Plugin(
        id = "nucleo-velocity",
        name = "Nucleo Velocity",
        version = "1.0-SNAPSHOT",
        description = "Nucleo Core for Velocity"
)
public class NucleoVelocityPlugin {
    @Getter
    private static NucleoVelocityPlugin plugin;

    private final ModuleClassLoader moduleClassLoader = ModuleClassLoader.create(
            NucleoVelocityPlugin.class.getClassLoader()
    );

    private final ModuleLoader moduleLoader;
    private final ProxyServer proxyServer;
    private final Logger logger;

    private final VelocityCommandHandler commandHandler;

    @Inject
    public NucleoVelocityPlugin(ProxyServer proxyServer, Logger logger) {
        plugin = this;

        this.commandHandler = VelocityCommandHandler.create(
                this,
                proxyServer
        );
        this.commandHandler.setExceptionHandler(new NucleoVelocityExceptionHandler(proxyServer));

        moduleLoader = initializeModuleLoaderAndLoadModules(logger);

        this.proxyServer = proxyServer;
        this.logger = logger;

        logger.info("Nucleo Velocity loaded");
    }

    @NotNull
    private ModuleLoader initializeModuleLoaderAndLoadModules(Logger logger) {
        ModuleLoader moduleLoader = ModuleLoader.create(logger);
        ModuleLoader.classLoader(moduleClassLoader);
        moduleLoader.loadModulesFromFolder();
        moduleLoader.loadModules();
        return moduleLoader;
    }

    public void registerCommands(Object... commands) {
        commandHandler.register(commands);
    }

    public void registerListener(Object... listeners) {
        for (Object listener : listeners) {
            proxyServer.getEventManager().register(this, listener);
        }
    }

    public AutoCompleter autoCompleter() {
        return commandHandler.getAutoCompleter();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        moduleLoader.enableModules();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        moduleLoader.disableModules();
    }
}