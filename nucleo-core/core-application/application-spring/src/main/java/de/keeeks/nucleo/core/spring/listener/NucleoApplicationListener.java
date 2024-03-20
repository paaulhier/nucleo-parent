package de.keeeks.nucleo.core.spring.listener;

import de.keeeks.nucleo.core.spring.module.ReflectionsModuleLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class NucleoApplicationListener implements ApplicationListener<ApplicationEvent> {
    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    private static final AtomicBoolean loaded = new AtomicBoolean(false);
    private static final AtomicBoolean enabled = new AtomicBoolean(false);

    private final Logger logger = Logger.getLogger(NucleoApplicationListener.class.getName());

    private final ReflectionsModuleLoader moduleLoader;

    @Override
    @EventListener
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent contextRefreshedEvent) {
            if (!initialized.getAndSet(true)) {
                moduleLoader.initialize();
            }
            if (!loaded.getAndSet(true)) {
                moduleLoader.loadModules();
            }
            if (!enabled.getAndSet(true)) {
                moduleLoader.enableModules();
            }
        }
    }
}