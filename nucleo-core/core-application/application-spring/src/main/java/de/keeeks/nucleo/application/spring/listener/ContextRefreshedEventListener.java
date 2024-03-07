package de.keeeks.nucleo.application.spring.listener;

import de.keeeks.nucleo.application.spring.SpringModule;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.application.command.Console;
import de.keeeks.nucleo.core.application.command.command.CommandRegistry;
import de.keeeks.nucleo.core.loader.ModuleLoader;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    private final ModuleLoader moduleLoader;
    private boolean modulesLoaded = false;

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        SpringModule.context(
                (AnnotationConfigServletWebServerApplicationContext) event.getApplicationContext()
        );
        if (!modulesLoaded) {
            ServiceRegistry.service(Console.class).startLineReading();
            ServiceRegistry.service(CommandRegistry.class).registerCommands();
            moduleLoader.loadModulesFromFolder();
            moduleLoader.loadModules();
            moduleLoader.enableModules();
            modulesLoaded = true;
        }
    }
}