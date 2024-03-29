package de.keeeks.nucleo.core.loader;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ModuleLogger;
import de.keeeks.nucleo.core.api.ModuleState;
import lombok.RequiredArgsConstructor;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public abstract class AbstractModuleLoader {
    protected final List<ModuleContainer> moduleContainers = new ArrayList<>();

    protected final Logger logger;

    public void enableModules() {
        sortedModules().forEachRemaining(this::enableModule);
    }

    protected void initializeModule(Class<?> clazz) throws
            InstantiationException,
            IllegalAccessException,
            InvocationTargetException,
            NoSuchMethodException {
        Module module = (Module) clazz.getConstructor().newInstance();
        ModuleLogger moduleLogger = ModuleLogger.create(module, logger);
        module.updateLogger(moduleLogger);
        moduleContainers.add(ModuleContainer.create(
                module
        ));
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

    protected TopologicalOrderIterator<ModuleContainer, DefaultEdge> sortedModules() {
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
            try {
                if (!graph.addEdge(dependency, moduleContainer, new DefaultEdge())) {
                    printDependencyInfoIfNecessary(moduleContainer, depend, graph, soft);
                }
            } catch (Throwable throwable) {
                printDependencyInfoIfNecessary(moduleContainer, depend, graph, soft);
            }
        } else {
            if (!soft) printMissingDependencySetUnavailableRemoveVertex(
                    graph,
                    moduleContainer,
                    depend
            );
        }
    }

    private void printDependencyInfoIfNecessary(
            ModuleContainer moduleContainer,
            String depend,
            Graph<ModuleContainer, DefaultEdge> graph,
            boolean soft
    ) {
        if (soft) return;
        printMissingDependencySetUnavailableRemoveVertex(
                graph,
                moduleContainer,
                depend
        );
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

}