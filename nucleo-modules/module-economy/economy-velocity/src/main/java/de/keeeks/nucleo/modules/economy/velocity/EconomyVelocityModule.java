package de.keeeks.nucleo.modules.economy.velocity;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.economy.api.EconomyApi;
import de.keeeks.nucleo.modules.economy.shared.NucleoEconomyApi;
import de.keeeks.nucleo.modules.economy.velocity.commands.EconomyCommand;

@ModuleDescription(
        name = "economy",
        depends = {"messaging", "config", "database-mysql"},
        softDepends = "players"
)
public class EconomyVelocityModule extends VelocityModule {
    private EconomyApi economyApi;

    @Override
    public void load() {
        this.economyApi = ServiceRegistry.registerService(
                EconomyApi.class,
                new NucleoEconomyApi(this)
        );
    }

    @Override
    public void enable() {
        if (Module.isAvailable("players")) {
            autoCompleter().registerSuggestion(
                    "economies",
                    (list, commandActor, executableCommand) -> economyApi.economies().stream().map(Economy::name).toList()
            );

            registerCommands(new EconomyCommand());
        }
    }
}