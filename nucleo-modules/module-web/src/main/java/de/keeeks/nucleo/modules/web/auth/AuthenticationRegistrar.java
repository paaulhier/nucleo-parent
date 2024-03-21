package de.keeeks.nucleo.modules.web.auth;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.web.WebModule;
import de.keeeks.nucleo.modules.web.configuration.WebConfiguration;

public final class AuthenticationRegistrar {
    private static final WebModule module = Module.module(WebModule.class);

    public static void registerAuthentication(AuthenticationHandler authenticationHandler) {
        var authenticationType = module.webConfiguration().authenticationType();
        if (authenticationType.equals(WebConfiguration.AuthenticationType.CUSTOM)) {
            module.javalin().before(authenticationHandler);
            return;
        }

        module.logger().severe("A custom authentication handler was registered, " +
                "but the authentication type is not set to CUSTOM." +
                " Ignoring custom handler.");
    }
}