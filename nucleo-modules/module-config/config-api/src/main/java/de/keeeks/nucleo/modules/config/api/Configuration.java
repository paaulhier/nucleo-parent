package de.keeeks.nucleo.modules.config.api;

import de.keeeks.nucleo.core.api.Module;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @param <H> the handle of the configuration
 */
@Getter
public abstract class Configuration<H> {
    protected final Module module;

    protected final File file;
    protected final H handle;

    protected Configuration(File file, H handle) {
        this.file = file;
        this.handle = handle;
        this.module = Module.module("config");
    }

    public void checkFile() {
        try {
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                if (Files.notExists(parentFile.toPath())) {
                    Files.createDirectories(parentFile.toPath());
                }
            }
            if (Files.notExists(file.toPath())) {
                Files.createFile(file.toPath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract <T> T loadObject(Class<T> clazz);

    public <T> T loadObject(Class<T> clazz, T defaultValue) {
        T object = loadObject(clazz);
        return object == null ? saveObject(defaultValue) : object;
    }

    public abstract <T> T saveObject(T t);
}