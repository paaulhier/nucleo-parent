package de.keeeks.nucleo.modules.web.auth;

import de.keeeks.nucleo.modules.web.configuration.authorization.BasicAuthenticationConfiguration;
import io.javalin.http.Context;
import io.javalin.security.BasicAuthCredentials;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasicAuthenticationHandler extends AuthenticationHandler {
    private final BasicAuthenticationConfiguration basicAuthorizationConfiguration;

    @Override
    public void handleAuthentication(
            Context context,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        BasicAuthCredentials basicAuthCredentials = context.basicAuthCredentials();
        if (basicAuthCredentials == null) {
            logger.info("User tried to authenticate without credentials");
            unauthorized();
            return;
        }

        if (!basicAuthCredentials.getUsername().equals(basicAuthorizationConfiguration.username())) {
            logger.info("User %s tried to authenticate with wrong password".formatted(
                    basicAuthCredentials.getUsername()
            ));
            unauthorized();
            return;
        }
        if (!basicAuthCredentials.getPassword().equals(basicAuthorizationConfiguration.password())) {
            logger.info("User %s tried to authenticate with wrong password".formatted(
                    basicAuthCredentials.getUsername()
            ));
            unauthorized();
        }
    }
}