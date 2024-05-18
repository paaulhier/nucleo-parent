package de.keeeks.nucleo.modules.notifications.spigot;

import de.keeeks.nucleo.core.api.Dependency;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import de.keeeks.nucleo.modules.notifications.shared.NucleoNotificationApi;
import de.keeeks.nucleo.modules.notifications.spigot.vulcan.NucleoVulcanNotificationListener;
import org.bukkit.Bukkit;

@ModuleDescription(
        name = "notifications",
        dependencies = {
                @Dependency(name = "config"),
                @Dependency(name = "messaging"),
                @Dependency(name = "database-mysql")
        },
        description = "Provides notifications for various events"
)
public class NotificationsSpigotModule extends SpigotModule {

    @Override
    public void load() {
        ServiceRegistry.registerService(
                NotificationApi.class,
                new NucleoNotificationApi(this)
        );
    }

    @Override
    public void enable() {
        if (Bukkit.getPluginManager().getPlugin("Vulcan") != null) {
            registerListener(new NucleoVulcanNotificationListener());
            logger.info("Found Vulcan! Registered Vulcan notification listener.");
        }
    }
}