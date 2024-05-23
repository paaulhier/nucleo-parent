package de.keeeks.nucleo.modules.vanish.velocity;

import de.keeeks.nucleo.core.api.Dependency;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.vanish.api.VanishApi;
import de.keeeks.nucleo.modules.vanish.shared.NucleoVanishApi;
import de.keeeks.nucleo.modules.vanish.velocity.commands.VanishCommand;
import de.keeeks.nucleo.modules.vanish.velocity.listener.NucleoVanishPlayerDisconnectListener;

@ModuleDescription(
        name = "vanish",
        dependencies = {
                @Dependency(name = "messaging"),
                @Dependency(name = "players")
        }
)
public class VanishVelocityModule extends VelocityModule {
    private VanishApi vanishApi;

    @Override
    public void load() {
        this.vanishApi = ServiceRegistry.registerService(
                VanishApi.class,
                new NucleoVanishApi()
        );
    }

    @Override
    public void enable() {
        registerCommands(new VanishCommand());
        registerListener(new NucleoVanishPlayerDisconnectListener(vanishApi));
    }
}