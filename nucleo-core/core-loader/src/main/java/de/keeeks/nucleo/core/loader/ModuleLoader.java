package de.keeeks.nucleo.core.loader;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleLogger;
import de.keeeks.nucleo.core.loader.classloader.ModuleClassLoader;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

public final class ModuleLoader extends AbstractModuleLoader {
    private static final Yaml yaml = new Yaml();
    @Setter
    private static ModuleClassLoader classLoader;

    private ModuleLoader(Logger logger) {
        super(logger);
    }


    public void loadModule(File file) {
        try {
            try (JarFile jarFile = new JarFile(file)) {
                ZipEntry moduleYamlEntry = jarFile.getEntry("module.yml");
                if (moduleYamlEntry == null) {
                    loadFailure("No module.yml found.", jarFile);
                    return;
                }

                try (var inputStream = jarFile.getInputStream(moduleYamlEntry)) {
                    Map<Object, Object> map = yaml.load(inputStream);
                    var mainClass = map == null ? null : map.get("main");

                    if (mainClass == null) {
                        loadFailure("No main class set!", jarFile);
                        return;
                    }

                    boolean mainClassFound = false;
                    for (JarEntry jarEntry : Collections.list(jarFile.entries())) {
                        var className = readAbleFileName(jarEntry);
                        if (className == null) continue;
                        if (!className.equals(mainClass)) continue;

                        try {
                            Class<?> clazz = Class.forName(
                                    className,
                                    false,
                                    classLoader
                            );
                            logger.info("Loaded class " + clazz.getName());
                            mainClassFound = true;

                            initializeModule(clazz);
                        } catch (Throwable e) {
                            loadFailure(
                                    "Could not create an instance of class %s".formatted(mainClass),
                                    jarFile,
                                    e
                            );
                        }
                    }
                    if (!mainClassFound) {
                        loadFailure(
                                "Could not find main class %s".formatted(mainClass),
                                jarFile
                        );
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadModulesFromFolder() {
        try {
            File folder = new File("nucleo", "modules");
            if (Files.notExists(folder.toPath())) {
                Files.createDirectories(folder.toPath());
            }
            var files = FileUtils.listJarFiles(folder);
            for (File file : files) {
                classLoader.addURL(file.toURI().toURL());
            }

            files.forEach(this::loadModule);
        } catch (IOException e) {
            logger.log(
                    Level.SEVERE,
                    "Failed to load modules.",
                    e
            );
        }
    }

    private String readAbleFileName(JarEntry jarEntry) {
        String jarEntryName = jarEntry.getName();
        if (!jarEntryName.endsWith(".class")) return null;
        return jarEntryName.substring(0, jarEntryName.length() - 6).replace('/', '.');
    }

    private void loadFailure(String moduleName, JarFile jarFile) {
        loadFailure(moduleName, jarFile, null);
    }

    private void loadFailure(String moduleName, JarFile jarFile, Throwable throwable) {
        logger.log(
                Level.SEVERE,
                "Failed to load module %s. Cause: %s.".formatted(jarFile.getName(), moduleName),
                throwable
        );
    }

    public static ModuleLoader create(Logger logger) {
        return new ModuleLoader(logger);
    }
}