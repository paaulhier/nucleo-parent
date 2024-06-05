package de.keeeks.nucleo.core.api;

import de.keeeks.nucleo.core.api.logger.NucleoLogger;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.api.utils.discord.DiscordWebhook;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class NucleoUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String webhookUrl = "https://ptb.discord.com/api/webhooks/1247971800583897180/" +
            "S7j79TrY36eqv3RhqrBtP0cHA02dHNrSU0ZRIHF16kS9ClEtRRYqJPx19QY432jAvcJP";

    private final Logger logger = NucleoLogger.logger();

    private final Map<Throwable, Long> lastException = new HashMap<>();

    private final String serviceName = ServiceRegistry.serviceName();

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (lastException.containsKey(e)) {
            long last = lastException.get(e);
            long now = System.currentTimeMillis();

            if (Duration.between(
                    java.time.Instant.ofEpochMilli(last),
                    java.time.Instant.ofEpochMilli(now)
            ).toMinutes() >= 5) {
                sendDiscordMessage(e);
                lastException.put(e, System.currentTimeMillis());
            }
        } else {
            sendDiscordMessage(e);
            lastException.put(e, System.currentTimeMillis());
        }

        logger.log(
                java.util.logging.Level.SEVERE,
                "Uncaught exception in thread %s".formatted(t.getName()),
                e
        );
    }

    private void sendDiscordMessage(Throwable e) {
        Scheduler.runAsync(() -> {
            DiscordWebhook webhook = new DiscordWebhook(webhookUrl);
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("Uncaught exception")
                    .setDescription("An uncaught exception occurred in the Nucleo core")
                    .addField("Exception", e.getClass().getName(), false)
                    .addField("Message", e.getMessage(), false)
                    .addField("Service", serviceName, false)
                    .addField("Stacktrace", Arrays.toString(e.getStackTrace()), false)
            );
            webhook.execute();
        });
    }
}