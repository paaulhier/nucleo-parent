package de.keeeks.nucleo.core.api;

import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.text;

@Getter
public abstract class Module {
    @Getter
    private static final List<Module> modules = new ArrayList<>();

    @Getter
    private static String serviceName;

    private final ModuleDescription description;
    private final File dataFolder;

    protected ModuleState moduleState = ModuleState.INITIALIZED;
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
        if (serviceName != null) return;
        File serviceFile = new File("nucleo.properties");
        if (!serviceFile.exists()) return;
        Properties properties = new Properties();
        try (var reader = Files.newBufferedReader(serviceFile.toPath())) {
            properties.load(reader);
            serviceName = properties.getProperty("service.name");
        }
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

    protected <T> void registerService(Class<T> service, T implementation) {
        ServiceRegistry.registerService(service, implementation);
    }

    protected <T> T service(Class<T> serviceClass) {
        return ServiceRegistry.service(serviceClass);
    }

    @SneakyThrows
    public void updateState(ModuleState moduleState) {
        Class<?> callerClass = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        String callerClassName = callerClass.getName();
        if (!callerClassName.equals("de.keeeks.nucleo.core.loader.ModuleLoader")
                && !callerClassName.equals("de.keeeks.nucleo.core.loader.AbstractModuleLoader")) {
            throw new IllegalStateException("You are not allowed to change the module state from outside the module loader");
        }

        this.moduleState = moduleState;
    }

    public Module updateLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public File dataFolder() {
        if (!dataFolder.exists()) dataFolder.mkdirs();
        return dataFolder;
    }

    public static Component modulesAsComponent() {
        return join(
                JoinConfiguration.commas(true),
                modules.stream().map(module -> {
                    ModuleDescription moduleDescription = module.description();
                    return text(
                            moduleDescription.name(),
                            colorByState(module)
                    ).hoverEvent(HoverEvent.showText(
                            hoverComponent(module, moduleDescription)
                    ));
                }).collect(Collectors.toList())
        );
    }

    @NotNull
    private static Component hoverComponent(Module module, ModuleDescription moduleDescription) {
        Component nameComponent = grayComponent("Name: ").append(text(
                moduleDescription.name(),
                colorByState(module)
        ));
        Component stateComponent = grayComponent(
                "State: "
        ).append(text(
                module.moduleState().display(),
                colorByState(module)
        ));
        Component dependenciesComponent = grayComponent(
                "Dependencies: "
        ).append(join(
                JoinConfiguration.commas(true),
                dependencies(moduleDescription, Dependency::required)
        ));
        Component softDependenciesComponent = grayComponent(
                "Soft dependencies: "
        ).append(join(
                JoinConfiguration.commas(true),
                dependencies(moduleDescription, dependency -> !dependency.required())
        ));
        Component versionComponent = grayComponent(
                "Version: "
        ).append(text(moduleDescription.version()));

        return join(
                JoinConfiguration.newlines(),
                Arrays.asList(
                        nameComponent,
                        stateComponent,
                        dependenciesComponent,
                        softDependenciesComponent,
                        versionComponent
                )
        );
    }

    private static @NotNull List<? extends Component> dependencies(
            ModuleDescription moduleDescription,
            Predicate<Dependency> predicate
    ) {
        return Arrays.stream(moduleDescription.dependencies()).filter(
                predicate
        ).map(dependency -> text(dependency.name())).toList();
    }

    @NotNull
    private static TextComponent grayComponent(String text) {
        return text(
                text,
                NamedTextColor.GRAY
        );
    }

    private static Component arrayAsComponent(String[] array) {
        return join(
                JoinConfiguration.commas(true),
                Arrays.stream(array).map(
                        dependency -> text(dependency, NamedTextColor.GRAY)
                ).collect(Collectors.toSet())
        );
    }

    private static TextColor colorByState(Module module) {
        if (module.moduleState() == ModuleState.INITIALIZED) {
            return NamedTextColor.YELLOW;
        }

        if (module.moduleState() == ModuleState.ENABLED) {
            return NamedTextColor.GREEN;
        }

        return NamedTextColor.RED;
    }

    public static boolean isAvailable(String name) {
        return module(name) != null;
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