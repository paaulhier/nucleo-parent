package de.keeeks.nucleo.core.spring;

import de.keeeks.nucleo.core.spring.module.ReflectionsModuleLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.logging.Logger;

@SpringBootApplication
public class CoreSpringBootApplication {
    private static final Logger logger = Logger.getLogger(CoreSpringBootApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(CoreSpringBootApplication.class, args);
    }

    @Bean
    public ReflectionsModuleLoader moduleLoader() {
        return new ReflectionsModuleLoader(logger);
    }

}