package de.keeeks.nucleo.cloudnet.listener.discord;

import de.keeeks.nucleo.cloudnet.NucleoCloudNetConfiguration;
import de.keeeks.nucleo.cloudnet.discord.DiscordWebhook;
import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.driver.service.ServiceLifeCycle;
import eu.cloudnetservice.node.event.service.CloudServicePostLifecycleEvent;
import eu.cloudnetservice.node.service.CloudService;

import java.awt.*;
import java.io.IOException;

public class DiscordCloudServiceStopListener extends DiscordCloudServiceListener {

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
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
