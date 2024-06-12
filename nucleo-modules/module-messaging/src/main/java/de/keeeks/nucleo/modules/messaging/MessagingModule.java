package de.keeeks.nucleo.modules.messaging;

import de.keeeks.nucleo.core.api.Dependency;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import lombok.Getter;

@Getter
@ModuleDescription(
        name = "messaging",
        dependencies = @Dependency(name = "config")
)
public class MessagingModule extends Module {

    private NatsConnection defaultNatsConnection;

    @Override
    public void load() {
        this.defaultNatsConnection = NatsConnection.create(
                logger(),
                JsonConfiguration.create(
                        dataFolder(),
                        "connection"
                ).loadObject(
                        NatsCredentials.class,
                        NatsCredentials.createDefault()
                )
        );
        ServiceRegistry.registerService(NatsConnection.class, defaultNatsConnection);
    }

    @Override
    public void disable() {
        NucleoNatsConnection.closeAllConnections();
    }
}