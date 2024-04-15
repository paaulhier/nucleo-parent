package de.keeeks.nucleo.modules.web.handler.socket;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.web.WebModule;
import io.javalin.websocket.*;
import lombok.Getter;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

@Getter
public abstract class SocketHandler {
    private static final WebModule module = Module.module(WebModule.class);
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SocketHandler.class);

    private final Logger logger;
    private final String path;

    public SocketHandler(String path) {
        this.logger = Logger.getLogger(path);
        this.logger.setParent(module.logger());
        this.logger.setUseParentHandlers(true);

        this.path = path;

        logger.info("Registered socket handler for %s".formatted(path));
    }

    public abstract void onMessage(WsMessageContext messageContext);

    public void onBinaryMessage(WsBinaryMessageContext binaryMessageContext) {
        logger.fine("Received binary message from %s".formatted(
                binaryMessageContext.session.getLocalAddress()
        ));
    }

    public void onConnect(WsConnectContext connectContext) {
        logger.fine("Connection established with %s".formatted(
                connectContext.session.getLocalAddress()
        ));
    }

    public void onClose(WsCloseContext closeContext) {
        logger.fine("Connection closed with %s due to %s".formatted(
                closeContext.session.getLocalAddress(),
                closeContext.closeStatus().name()
        ));
    }

    public abstract void onError(WsErrorContext errorContext);
}