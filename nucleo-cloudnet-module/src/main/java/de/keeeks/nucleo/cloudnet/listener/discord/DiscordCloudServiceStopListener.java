package de.keeeks.nucleo.cloudnet.listener.discord;

import de.keeeks.nucleo.cloudnet.NucleoCloudNetConfiguration;
import de.keeeks.nucleo.cloudnet.discord.DiscordWebhook;
import eu.cloudnetservice.common.log.LogManager;
import eu.cloudnetservice.common.log.Logger;
import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.driver.service.ServiceLifeCycle;
import eu.cloudnetservice.node.event.service.CloudServicePostLifecycleEvent;
import eu.cloudnetservice.node.event.service.CloudServicePostPrepareEvent;
import eu.cloudnetservice.node.service.CloudService;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiscordCloudServiceStopListener extends DiscordCloudServiceListener{

    private final NucleoCloudNetConfiguration configuration;

    public DiscordCloudServiceStopListener(NucleoCloudNetConfiguration configuration) {
        this.configuration = configuration;
    }

    @EventListener
    public void handleServiceStart(CloudServicePostLifecycleEvent event) {
        if (!event.newLifeCycle().equals(ServiceLifeCycle.DELETED)) return;
        CloudService service = event.service();

        executorService.submit(() -> {
            DiscordWebhook discordWebhook = new DiscordWebhook(configuration.discordWebhookUrl());

            discordWebhook.addEmbed(createDiscordEmbed(
                    "Service stop",
                    Color.red,
                    service
            ));

            try {
                discordWebhook.execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
