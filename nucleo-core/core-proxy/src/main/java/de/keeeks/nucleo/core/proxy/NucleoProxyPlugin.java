package de.keeeks.nucleo.core.proxy;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.loader.ModuleLoader;
import de.keeeks.nucleo.core.loader.classloader.ModuleClassLoader;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;
import revxrsal.commands.autocomplete.AutoCompleter;
import revxrsal.commands.bungee.BungeeCommandHandler;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class NucleoProxyPlugin extends Plugin {
    @Getter
    private static NucleoProxyPlugin plugin;

    private final List<Object> commandRegistrations = new LinkedList<>();

    private final ModuleClassLoader moduleClassLoader = ModuleClassLoader.create(
            NucleoProxyPlugin.class.getClassLoader()
    );
    private final ModuleLoader moduleLoader = ModuleLoader.create(
            this.getLogger()
    );

    private final BungeeCommandHandler bungeeCommandHandler = BungeeCommandHandler.create(
            this
    );
    @Getter
    private final BungeeAudiences bungeeAudiences = BungeeAudiences.create(
            this
    );

    @Getter
    private final long startupTime = System.currentTimeMillis();
    @Getter
    private Duration startupDuration;

    private boolean enabled = false;

    @Override
    public void onLoad() {
        plugin = this;

        ModuleLoader.classLoader(moduleClassLoader);
        moduleLoader.loadModulesFromFolder();
        moduleLoader.loadModules();

        bungeeCommandHandler.getAutoCompleter().registerSuggestion("@booleans", List.of(
                "true", "false"
        ));
    }

    @Override
    public void onEnable() {
        enabled = true;

        bungeeCommandHandler.register(commandRegistrations);

        bungeeCommandHandler.registerContextResolver(
                Audience.class,
                contextResolverContext -> {
                    UUID uniqueId = contextResolverContext.actor().getUniqueId();
                    return bungeeAudiences.player(uniqueId);
                }
        );

        moduleLoader.enableModules();
        startupDuration = Duration.ofMillis(
                System.currentTimeMillis() - startupTime
        );

        Module.modules().forEach(Module::postStartup);
    }

    @Override
    public void onDisable() {
        Scheduler.shutdown();
        enabled = false;
        moduleLoader.disableModules();
    }

    public void registerCommands(Object... commands) {
        if (!enabled) {
            commandRegistrations.addAll(List.of(commands));
            getLogger().info("Command registration of %s delayed until plugin is enabled.".formatted(
                    Arrays.stream(commands).map(
                            o -> o.getClass().getName()
                    ).collect(Collectors.joining(", "))
            ));
            return;
        }
        bungeeCommandHandler.register(commands);
    }

    public AutoCompleter autoCompleter() {
        return bungeeCommandHandler.getAutoCompleter();
    }
}