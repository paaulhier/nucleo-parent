package de.keeeks.nucleo.cloudnet.listener.discord;

import de.keeeks.nucleo.cloudnet.NucleoCloudNetConfiguration;
import de.keeeks.nucleo.cloudnet.discord.DiscordWebhook;
import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.node.event.service.CloudServicePostPrepareEvent;
import eu.cloudnetservice.node.service.CloudService;
import jakarta.inject.Singleton;

import java.awt.*;

@Singleton
public class DiscordCloudServiceStartListener extends DiscordCloudServiceListener {
    private final NucleoCloudNetConfiguration configuration;

    public DiscordCloudServiceStartListener(NucleoCloudNetConfiguration configuration) {
        this.configuration = configuration;
    }

    @EventListener
    public void handleServiceStart(CloudServicePostPrepareEvent event) {
        CloudService service = event.service();

        executorService.submit(() -> {
            DiscordWebhook discordWebhook = new DiscordWebhook(configuration.discordWebhookUrl());

            discordWebhook.addEmbed(createDiscordEmbed(
                    "Service start",
                    Color.green,
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
