package de.keeeks.nucleo.cloudnet;

public record NucleoCloudNetConfiguration(
        boolean publishDiscordWebhook,
        String discordWebhookUrl
) {
}