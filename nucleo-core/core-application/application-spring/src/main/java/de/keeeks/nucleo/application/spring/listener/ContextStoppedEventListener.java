package de.keeeks.nucleo.application.spring.listener;

import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.loader.ModuleLoader;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContextStoppedEventListener implements ApplicationListener<ContextStoppedEvent> {
    private final ModuleLoader moduleLoader;
    @Override
    public void onApplicationEvent(@NotNull ContextStoppedEvent event) {
        Scheduler.shutdown();
        moduleLoader.disableModules();
    }
}