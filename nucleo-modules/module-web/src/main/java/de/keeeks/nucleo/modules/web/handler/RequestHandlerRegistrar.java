package de.keeeks.nucleo.modules.web.handler;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.web.WebModule;
import io.javalin.http.HandlerType;

public final class RequestHandlerRegistrar {
    private static final WebModule webModule = Module.module(WebModule.class);

    public static void register(RequestHandler... requestHandlers) {
        for (RequestHandler requestHandler : requestHandlers) {
            for (HandlerType supportedHandlerType : requestHandler.supportedHandlerTypes()) {
                webModule.javalin().addHttpHandler(
                        supportedHandlerType,
                        requestHandler.path(),
                        requestHandler
                );
            }
        }
    }
}