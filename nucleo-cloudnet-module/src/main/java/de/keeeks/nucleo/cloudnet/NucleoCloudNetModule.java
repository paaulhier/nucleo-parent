package de.keeeks.nucleo.cloudnet;

import de.keeeks.nucleo.cloudnet.listener.CloudServiceStartListener;
import de.keeeks.nucleo.cloudnet.listener.discord.DiscordCloudServiceStartListener;
import de.keeeks.nucleo.cloudnet.listener.discord.DiscordCloudServiceStopListener;
import eu.cloudnetservice.common.log.LogManager;
import eu.cloudnetservice.common.log.Logger;
import eu.cloudnetservice.driver.document.Document;
import eu.cloudnetservice.driver.document.DocumentFactory;
import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.driver.module.ModuleLifeCycle;
import eu.cloudnetservice.driver.module.ModuleTask;
import eu.cloudnetservice.driver.module.driver.DriverModule;
import jakarta.inject.Singleton;
import lombok.NonNull;

import java.nio.file.Files;

@Singleton
public class NucleoCloudNetModule extends DriverModule {
    private final Logger logger = LogManager.logger(NucleoCloudNetModule.class);

    @ModuleTask(lifecycle = ModuleLifeCycle.STARTED)
    public void finishStartup(@NonNull EventManager eventManager) {
        eventManager.registerListener(CloudServiceStartListener.class);
        Document document = readConfig(DocumentFactory.json());
        if (Files.notExists(configPath())) {
            writeConfig(document.mutableCopy().appendTree(new NucleoCloudNetConfiguration(
                    false,
                    ""
            )));
        }

        NucleoCloudNetConfiguration nucleoCloudNetConfiguration = document.toInstanceOf(NucleoCloudNetConfiguration.class);

        if (nucleoCloudNetConfiguration.publishDiscordWebhook()) {
            eventManager.registerListeners(
                    new DiscordCloudServiceStartListener(nucleoCloudNetConfiguration),
                    new DiscordCloudServiceStopListener(nucleoCloudNetConfiguration)
            );
        }
    }
}