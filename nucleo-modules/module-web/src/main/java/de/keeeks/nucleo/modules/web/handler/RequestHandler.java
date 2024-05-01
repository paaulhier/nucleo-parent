package de.keeeks.nucleo.modules.web.handler;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.modules.web.WebModule;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public abstract class RequestHandler implements Handler {
    protected final Logger logger = Module.module(WebModule.class).logger();

    private final List<HandlerType> supportedHandlerTypes;
    private final String path;

    protected RequestHandler(String path, HandlerType... handlerType) {
        this.path = path;
        this.supportedHandlerTypes = Arrays.asList(handlerType);
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        HandlerType method = context.method();

        if (supportedHandlerTypes.contains(method)) {
            Scheduler.runAsync(() -> {
                try {
                    processRequest(context);
                } catch (Exception e) {
                    logger.log(
                            Level.SEVERE,
                            "An error occurred while processing a request on path " + path,
                            e
                    );
                }
            });
        } else {
            context.status(405);
        }
    }

    public abstract void processRequest(Context context) throws Exception;

    public enum RequestMethod {
        GET,
        POST,
        PUT,
        DELETE,
        PATCH,
        OPTIONS,
        HEAD
    }
}