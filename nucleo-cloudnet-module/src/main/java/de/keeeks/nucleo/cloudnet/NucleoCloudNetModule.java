package de.keeeks.nucleo.cloudnet;

import de.keeeks.nucleo.cloudnet.listener.CloudServiceStartListener;
import eu.cloudnetservice.common.log.LogManager;
import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.driver.module.ModuleLifeCycle;
import eu.cloudnetservice.driver.module.ModuleTask;
import eu.cloudnetservice.driver.module.driver.DriverModule;
import jakarta.inject.Singleton;
import lombok.NonNull;

@Singleton
public class NucleoCloudNetModule extends DriverModule {

    @ModuleTask(lifecycle = ModuleLifeCycle.STARTED)
    public void finishStartup(@NonNull EventManager eventManager) {
        eventManager.registerListener(CloudServiceStartListener.class);
        LogManager.logger(NucleoCloudNetModule.class).info("registered listener");
    }
}