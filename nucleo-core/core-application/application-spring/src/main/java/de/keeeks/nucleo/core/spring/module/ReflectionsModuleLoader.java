package de.keeeks.nucleo.core.spring.module;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.loader.AbstractModuleLoader;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReflectionsModuleLoader extends AbstractModuleLoader {
    private static final Reflections reflections = new Reflections("de.keeeks");

    public ReflectionsModuleLoader(Logger logger) {
        super(logger);
    }

    public void initialize() {
        for (Class<? extends Module> clazz : reflections.getSubTypesOf(Module.class)) {
            try {
                initializeModule(clazz);
            } catch (Throwable e) {
                logger.log(
                        Level.SEVERE,
                        "Failed to initialize module %s".formatted(clazz.getName()),
                        e
                );
            }
        }
    }
}