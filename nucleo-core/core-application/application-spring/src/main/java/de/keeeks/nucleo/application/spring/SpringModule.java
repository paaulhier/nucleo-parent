package de.keeeks.nucleo.application.spring;

import de.keeeks.nucleo.core.api.Module;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

public class SpringModule extends Module {
    @Getter
    @Setter
    @Accessors(fluent = true)
    private static AnnotationConfigServletWebServerApplicationContext context;

    public <T> void registerComponent(T component) {
        context.getBeanFactory().registerSingleton(
                component.getClass().getSimpleName(),
                component
        );
    }
}