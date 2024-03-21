package de.keeeks.nucleo.modules.web;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.web.auth.BasicAuthenticationHandler;
import de.keeeks.nucleo.modules.web.configuration.WebConfiguration;
import de.keeeks.nucleo.modules.web.configuration.authorization.BasicAuthenticationConfiguration;
import io.javalin.Javalin;
import lombok.Getter;

import java.io.File;

@Getter
@ModuleDescription(
        name = "web",
        description = "Provides all web related features",
        depends = "config"
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
    }

    @Override
    public void enable() {
        javalin = createJavalin();
        initializeAuthentication();
    }

    @Override
    public void disable() {
        if (javalin != null) {
            javalin.stop();
        }
    }

    private Javalin createJavalin() {
        if (webConfiguration.host() == null) {
            logger.info("No host specified, starting on localhost");
            return Javalin.create(javalinConfig -> {
                if (webConfiguration.devLogging()) {
                    javalinConfig.bundledPlugins.enableDevLogging();
                }
            }).start(webConfiguration.port());
        }
        logger.info("Starting on " + webConfiguration.host() + ":" + webConfiguration.port());
        return Javalin.create().start(
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