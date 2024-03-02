package de.keeeks.nucleo.core.proxy;

import de.keeeks.nucleo.core.loader.ModuleLoader;
import de.keeeks.nucleo.core.loader.classloader.ModuleClassLoader;
import de.keeeks.nucleo.core.proxy.commands.ModulesCommand;
import de.keeeks.nucleo.core.proxy.commands.UptimeCommand;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import revxrsal.commands.bungee.BungeeCommandHandler;
import revxrsal.commands.exception.CooldownException;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NucleoProxyPlugin extends Plugin {
    @Getter
    @Accessors(fluent = true)
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
    @Accessors(fluent = true)
    private final BungeeAudiences bungeeAudiences = BungeeAudiences.create(
            this
    );

    @Getter
    @Accessors(fluent = true)
    private final long startupTime = System.currentTimeMillis();
    @Getter
    @Accessors(fluent = true)
    private Duration startupDuration;

    private boolean enabled = false;

    @Override
    public void onLoad() {
        plugin = this;

        ModuleLoader.classLoader(moduleClassLoader);
        moduleLoader.loadModulesFromFolder();
        moduleLoader.loadModules();
    }

    @Override
    public void onEnable() {
        enabled = true;

        bungeeCommandHandler.register(commandRegistrations);

        registerCommands(
                new ModulesCommand(this),
                new UptimeCommand(this)
        );

        moduleLoader.enableModules();
        startupDuration = Duration.ofMillis(
                System.currentTimeMillis() - startupTime
        );
    }

    @Override
    public void onDisable() {
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
}