package de.keeeks.nucleo.cloudnet.listener.discord;

import de.keeeks.nucleo.cloudnet.discord.DiscordWebhook;
import eu.cloudnetservice.node.service.CloudService;

import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class DiscordCloudServiceListener {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
            "dd.MM.yyyy HH:mm:ss"
    ).withZone(ZoneId.systemDefault());

    protected final ExecutorService executorService = Executors.newFixedThreadPool(2);

    protected DiscordWebhook.EmbedObject createDiscordEmbed(String title, Color color, CloudService service) {
        return new DiscordWebhook.EmbedObject().setTitle(
                title
        ).setColor(
                color
        ).addField(
                "Name",
                service.serviceInfo().name(),
                true
        ).addField(
                "Node",
                service.serviceId().nodeUniqueId(),
                true
        ).addField(
                "Template",
                service.serviceId().taskName(),
                true
        ).addField(
                "IP",
                service.serviceInfo().address().host(),
                true
        ).addField(
                "Port",
                String.valueOf(service.serviceInfo().address().port()),
                true
        ).setFooter(
                dateTimeFormatter.format(Instant.now()),
                null
        );
    }

}