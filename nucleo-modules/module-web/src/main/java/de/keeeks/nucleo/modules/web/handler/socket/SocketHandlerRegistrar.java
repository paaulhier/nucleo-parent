package de.keeeks.nucleo.modules.web.handler.socket;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.web.WebModule;

public final class SocketHandlerRegistrar {
    private static final WebModule webModule = Module.module(WebModule.class);

    public static void register(SocketHandler... socketHandlers) {
        for (SocketHandler socketHandler : socketHandlers) {
            webModule.javalin().ws(socketHandler.path(), wsConfig -> {
                wsConfig.onConnect(socketHandler::onConnect);
                wsConfig.onClose(socketHandler::onClose);
                wsConfig.onMessage(socketHandler::onMessage);
                wsConfig.onBinaryMessage(socketHandler::onBinaryMessage);
                wsConfig.onError(socketHandler::onError);
            });
        }
    }
}