package de.keeeks.nucleo.modules.web;

import de.keeeks.nucleo.core.api.Dependency;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.web.auth.BasicAuthenticationHandler;
import de.keeeks.nucleo.modules.web.configuration.WebConfiguration;
import de.keeeks.nucleo.modules.web.configuration.authorization.BasicAuthenticationConfiguration;
import de.keeeks.nucleo.modules.web.handler.socket.SocketHandler;
import io.javalin.Javalin;
import io.javalin.util.JavalinLogger;
import lombok.Getter;

import java.io.File;

@Getter
@ModuleDescription(
        name = "web",
        description = "Provides all web related features",
        dependencies = {
                @Dependency(name = "config")
        }
)
public class WebModule extends Module {
    private WebConfiguration webConfiguration;
    private Javalin javalin;

    @Override
    public void load() {
        webConfiguration = JsonConfiguration.create(
                dataFolder(),
                "config"
        ).loadObject(
                WebConfiguration.class,
                WebConfiguration.defaultConfiguration()
        );
        JavalinLogger.enabled = false;
        JavalinLogger.startupInfo = false;
        javalin = Javalin.create(javalinConfig -> {
            if (webConfiguration.devLogging()) {
                javalinConfig.bundledPlugins.enableDevLogging();
            }
        });
    }

    @Override
    public void enable() {
        if (javalin != null) {
            startJavalinWebServer();
        }

        initializeAuthentication();
    }

    @Override
    public void disable() {
        SocketHandler.closeAllSessions();
        if (javalin != null) {
            javalin.stop();
        }
    }

    private void startJavalinWebServer() {
        if (webConfiguration.host() == null) {
            logger.info("No host specified, binding to localhost");
            javalin.start(webConfiguration.port());
            return;
        }
        logger.info("Binding to %s:%s".formatted(
                webConfiguration.host(),
                webConfiguration.port()
        ));
        javalin.start(
                webConfiguration.host(),
                webConfiguration.port()
        );
    }

    private void initializeAuthentication() {
        if (webConfiguration.authenticationType().equals(WebConfiguration.AuthenticationType.BASIC)) {
            var basicAuthenticationConfiguration = JsonConfiguration.create(
                    new File(dataFolder(), "auth"),
                    "auth"
            ).loadObject(
                    BasicAuthenticationConfiguration.class,
                    BasicAuthenticationConfiguration.defaultConfiguration()
            );

            javalin.before(new BasicAuthenticationHandler(
                    basicAuthenticationConfiguration
            ));
            logger.info("Basic authentication enabled");
            return;
        }

        logger.info("Using custom authentication handler");
    }
}