package de.keeeks.nucleo.modules.economy.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.economy.api.EconomyApi;
import de.keeeks.nucleo.modules.shared.NucleoEconomyApi;

@ModuleDescription(
        name = "economy",
        depends = {"messaging", "config", "database-mysql"}
)
public class EconomySpigotModule extends SpigotModule {
    @Override
    public void load() {
        ServiceRegistry.registerService(
                EconomyApi.class,
                new NucleoEconomyApi(this)
        );
    }
}