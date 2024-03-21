package de.keeeks.nucleo.modules.web.handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Getter
public abstract class RequestHandler implements Handler {
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
            processRequest(context);
        } else {
            context.status(405);
        }
    }

    public abstract void processRequest(Context context);

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