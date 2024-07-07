package de.keeeks.nucleo.core.spigot.paper;

import de.keeeks.nucleo.core.spigot.NucleoSpigotPlugin;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class NucleoPaperBootstrap implements PluginBootstrap {
    /**
     * Called by the server, allowing you to bootstrap the plugin with a context that provides things like a logger and your shared plugin configuration file.
     *
     * @param context the server provided context
     */
    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        return new NucleoSpigotPlugin();
    }
}