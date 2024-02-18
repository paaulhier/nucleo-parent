package de.keeeks.nucleo.core.api;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Getter
@Accessors(fluent = true)
public abstract class Module {
    @Getter
    private static final List<Module> modules = new ArrayList<>();

    private final ModuleDescription description;
    private final File dataFolder;

    protected ModuleState moduleState = ModuleState.INITIALIZED;
    protected String serviceName;
    protected Logger logger;

    @SneakyThrows
    protected Module() {
        Class<? extends Module> clazz = getClass();
        this.description = clazz.getAnnotation(ModuleDescription.class);
        this.dataFolder = new File("nucleo/modules/%s".formatted(
                description.name()
        ));

        modules.add(this);
        readServiceName();
    }

    private void readServiceName() throws IOException {
        File serviceFile = new File("serviceName");
        if (!serviceFile.exists()) return;
        this.serviceName = Files.readString(
                serviceFile.toPath()
        );
    }

    public void load() {
        //Does not have any function yet. Do a "module-override" to implement stuff for modules
    }

    public void enable() {
        //Does not have any function yet. Do a "module-override" to implement stuff for modules
    }

    public void disable() {
        //Does not have any function yet. Do a "module-override" to implement stuff for modules
    }

    public void postStartup() {
        //Does not have any function yet. Do a "module-override" to implement stuff for modules
    }

    public Module updateState(ModuleState moduleState) {
        this.moduleState = moduleState;
        return this;
    }

    public Module updateLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public File dataFolder() {
        if (!dataFolder.exists()) dataFolder.mkdirs();
        return dataFolder;
    }

    public static <T extends Module> T module(Class<T> clazz) {
        return (T) modules.stream()
                .filter(module -> module.getClass().equals(clazz))
                .findFirst()
                .orElse(null);
    }

    public static <T extends Module> T module(String name) {
        return (T) modules.stream()
                .filter(module -> module.description().name().equals(name))
                .findFirst()
                .orElse(null);
    }
}