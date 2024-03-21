package de.keeeks.nucleo.modules.web.auth;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.web.WebModule;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public abstract class AuthenticationHandler implements Handler {
    protected final Module module = Module.module(WebModule.class);
    protected final Logger logger = module.logger();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        context.async(() -> handleAuthentication(
                context,
                context.req(),
                context.res()
        ));
    }

    public abstract void handleAuthentication(
            Context context,
            HttpServletRequest request,
            HttpServletResponse response
    );

    public final void unauthorized() {
        throw new UnauthorizedResponse();
    }
}