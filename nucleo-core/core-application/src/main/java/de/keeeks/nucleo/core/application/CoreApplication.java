package de.keeeks.nucleo.core.application;

import de.keeeks.nucleo.core.loader.ModuleLoader;
import de.keeeks.nucleo.core.loader.classloader.ModuleClassLoader;

import java.util.logging.Logger;

public abstract class CoreApplication {
    private final ModuleLoader moduleLoader;

    public CoreApplication(Logger logger) {
        this.moduleLoader = ModuleLoader.create(logger);
        ModuleLoader.classLoader(ModuleClassLoader.create(
                getClass().getClassLoader()
        ));
        Runtime.getRuntime().addShutdownHook(new Thread(
                moduleLoader::disableModules
        ));
    }

    public void startup() {
        moduleLoader.loadModulesFromFolder();

        moduleLoader.loadModules();
    }

    public void enable() {
        moduleLoader.enableModules();
    }
}