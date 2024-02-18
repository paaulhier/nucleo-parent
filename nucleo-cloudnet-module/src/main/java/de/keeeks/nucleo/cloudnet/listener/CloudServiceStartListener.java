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

@Singleton
public final class CloudServiceStartListener {
    private final Logger logger = LogManager.logger(CloudServiceStartListener.class);

    @EventListener
    public void handleServiceStart(CloudServicePostPrepareEvent event) {
        var cloudService = event.service();
        File serverNameFile = new File(cloudService.directory().toFile(), "serviceName");
        if (!serverNameFile.exists()) {
            try (var fileWriter = new FileWriter(serverNameFile)) {
                fileWriter.write(cloudService.serviceId().name());
            } catch (IOException e) {
                printErrorMessageAndStopService(cloudService, e);
            }
        }
        if (!cloudService.serviceId().environment().readPropertyOrDefault(
                DocProperty.property("isJavaProxy", Boolean.class),
                false
        )) {
            File serverPropertiesFile = new File(
                    cloudService.directory().toFile(),
                    "server.properties"
            );
            var properties = new Properties();
            try (var reader = new FileReader(serverPropertiesFile)) {
                properties.load(reader);
            } catch (IOException e) {
                printErrorMessageAndStopService(cloudService, e);
            }
            properties.setProperty("server-name", cloudService.serviceId().name());
            try (var writer = new FileWriter(serverPropertiesFile)) {
                properties.store(
                        writer,
                        null
                );
            } catch (IOException e) {
                printErrorMessageAndStopService(cloudService, e);
            }
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