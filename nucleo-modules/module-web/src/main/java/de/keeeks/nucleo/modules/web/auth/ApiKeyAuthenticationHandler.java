package de.keeeks.nucleo.modules.web.auth;

import de.keeeks.nucleo.modules.web.configuration.authorization.ApiKeyAuthenticationConfiguration;
import io.javalin.http.Context;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiKeyAuthenticationHandler extends AuthenticationHandler {
    private final ApiKeyAuthenticationConfiguration configuration;

    @Override
    public void handleAuthentication(Context context, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String apiKey = context.header("X-Api-Key");
        if (apiKey == null) {
            logger.info("User tried to authenticate without api key");
            unauthorized();
            return;
        }

        if (!apiKey.equals(configuration.apiKey())) {
            logger.info("User tried to authenticate with wrong api key");
            unauthorized();
        }
    }
}