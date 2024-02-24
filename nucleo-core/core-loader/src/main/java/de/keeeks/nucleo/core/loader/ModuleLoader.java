package de.keeeks.nucleo.core.loader;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ModuleLogger;
import de.keeeks.nucleo.core.api.ModuleState;
import de.keeeks.nucleo.core.loader.classloader.ModuleClassLoader;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModuleLoader {
    private static final Yaml yaml = new Yaml();
    @Setter
    @Accessors(fluent = true)
    private static ModuleClassLoader classLoader;

    private final List<ModuleContainer> moduleContainers = new ArrayList<>();

    private final Logger logger;

    public void loadModule(File file) {
        try {
            classLoader.addURL(file.toURI().toURL());

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

                            Module module = (Module) clazz.getConstructor().newInstance();
                            ModuleLogger moduleLogger = ModuleLogger.create(module, logger);
                            module.updateLogger(moduleLogger);
                            moduleContainers.add(ModuleContainer.create(
                                    module
                            ));
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
            var files = FileUtils.listJarFiles(new File("nucleo", "modules"));
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

    public void enableModules() {
        sortedModules().forEachRemaining(this::enableModule);
    }

    private void enableModule(ModuleContainer moduleContainer) {
        Module module = moduleContainer.module();
        try {
            moduleOutput("Enabling", moduleContainer);
            module.enable();
            module.updateState(ModuleState.ENABLED);
        } catch (Throwable throwable) {
            disableModule(moduleContainer);
            module.updateState(ModuleState.FAILED_TO_ENABLE);
            logger.log(
                    Level.SEVERE,
                    "Failed to enabled module %s".formatted(moduleContainer.description().name()),
                    throwable
            );
        }
    }

    private void moduleOutput(String action, ModuleContainer moduleContainer) {
        ModuleDescription description = moduleContainer.description();
        logger.info((action + " module %s version %s by %s").formatted(
                description.name(),
                description.version(),
                Arrays.toString(description.authors())
        ));
    }

    public void loadModules() {
        sortedModules().forEachRemaining(this::loadModule);
    }

    private void loadModule(ModuleContainer moduleContainer) {
        Module module = moduleContainer.module();
        try {
            moduleOutput("Loading", moduleContainer);
            module.load();
            module.updateState(ModuleState.LOADED);
        } catch (Throwable throwable) {
            disableModule(moduleContainer);
            module.updateState(ModuleState.FAILED_TO_LOAD);
            logger.log(
                    Level.SEVERE,
                    "Failed to load module %s".formatted(moduleContainer.description().name()),
                    throwable
            );
        }
    }

    public void disableModules() {
        sortedModules().forEachRemaining(this::disableModule);
    }

    private void disableModule(ModuleContainer moduleContainer) {
        Module module = moduleContainer.module();
        try {
            moduleOutput("Disabling", moduleContainer);
            module.disable();
            module.updateState(ModuleState.DISABLED);
        } catch (Throwable throwable) {
            moduleContainer.available().set(false);
            module.updateState(ModuleState.FAILED_TO_DISABLE);
            logger.log(
                    Level.SEVERE,
                    "Failed to disable module %s".formatted(moduleContainer.description().name()),
                    throwable
            );
        }
    }

    private TopologicalOrderIterator<ModuleContainer, DefaultEdge> sortedModules() {
        Graph<ModuleContainer, DefaultEdge> graph = new DirectedAcyclicGraph<>(DefaultEdge.class);

        moduleContainers.stream().filter(
                moduleContainer -> moduleContainer.available().get()
        ).forEach(moduleContainer -> {
            var description = moduleContainer.module().description();

            graph.addVertex(moduleContainer);

            for (String depend : description.depends()) {
                handleDependency(moduleContainer, depend, graph, false);
            }
            for (String depend : description.softDepends()) {
                handleDependency(moduleContainer, depend, graph, true);
            }
        });

        return new TopologicalOrderIterator<>(graph);
    }

    private void handleDependency(
            ModuleContainer moduleContainer,
            String depend,
            Graph<ModuleContainer, DefaultEdge> graph,
            boolean soft
    ) {
        if (depend.isEmpty()) return;
        ModuleContainer dependency = dependency(depend);

        if (dependency != null) {
            graph.addVertex(dependency);
            if (!graph.addEdge(dependency, moduleContainer, new DefaultEdge())) {
                if (soft) return;
                printMissingDependencySetUnavailableRemoveVertex(
                        graph,
                        moduleContainer,
                        depend
                );
            }
        } else {
            if (!soft) printMissingDependencySetUnavailableRemoveVertex(
                    graph,
                    moduleContainer,
                    depend
            );
        }
    }

    private void printMissingDependencySetUnavailableRemoveVertex(
            Graph<ModuleContainer, DefaultEdge> graph,
            ModuleContainer moduleContainer,
            String depend
    ) {
        logger.severe("Could not load module %s. Missing dependency %s".formatted(
                moduleContainer.description().name(),
                depend
        ));
        moduleContainer.available().set(false);
        moduleContainer.module().updateState(
                ModuleState.UNAVAILABLE
        );
        graph.removeVertex(moduleContainer);
    }

    private ModuleContainer dependency(String dependency) {
        return moduleContainers.stream().filter(
                moduleContainer -> moduleContainer.description().name().equals(dependency)
        ).findFirst().orElse(null);
    }

    private boolean checkForClassFile(JarEntry jarEntry) {
        return !jarEntry.isDirectory() && jarEntry.getName().endsWith(".class");
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