package de.keeeks.nucleo.cloudnet.listener;

import eu.cloudnetservice.common.log.LogManager;
import eu.cloudnetservice.common.log.Logger;
import eu.cloudnetservice.driver.document.property.DocProperty;
import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.node.event.service.CloudServicePostPrepareEvent;
import eu.cloudnetservice.node.service.CloudService;
import jakarta.inject.Singleton;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.function.Consumer;

@Singleton
public final class CloudServiceStartListener {
    private final Logger logger = LogManager.logger(CloudServiceStartListener.class);

    @EventListener
    public void handleServiceStart(CloudServicePostPrepareEvent event) {
        var cloudService = event.service();
        loadAndSaveProperties(
                cloudService,
                "nucleo.properties",
                properties -> properties.setProperty("service.name", cloudService.serviceId().name())
        );
        if (!cloudService.serviceId().environment().readPropertyOrDefault(
                DocProperty.property("isJavaProxy", Boolean.class),
                false
        )) {
            loadAndSaveProperties(
                    cloudService,
                    "server.properties",
                    properties -> properties.setProperty("server-name", cloudService.serviceId().name())
            );
        }
    }

    private void loadAndSaveProperties(
            CloudService cloudService,
            String fileName,
            Consumer<Properties> consumer
    ) {
        File file = new File(cloudService.directory().toFile(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                printErrorMessageAndStopService(cloudService, e);
            }
        }

        var properties = new Properties();
        try (var reader = new FileReader(file)) {
            properties.load(reader);
            consumer.accept(properties);
        } catch (IOException e) {
            printErrorMessageAndStopService(cloudService, e);
        }
        try (var writer = new FileWriter(file)) {
            properties.store(
                    writer,
                    null
            );
        } catch (IOException e) {
            printErrorMessageAndStopService(cloudService, e);
        }
    }

    private void printErrorMessageAndStopService(CloudService cloudService, Throwable throwable) {
        cloudService.stop();
        cloudService.publishServiceInfoSnapshot();
        logger.severe(
                "The service %s could not get started.",
                throwable,
                cloudService.serviceId().name()
        );
    }
}