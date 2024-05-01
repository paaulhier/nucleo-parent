package de.keeeks.nucleo.modules.web.handler.socket;

import com.google.gson.Gson;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.modules.web.WebModule;
import io.javalin.websocket.*;
import lombok.Getter;
import org.eclipse.jetty.websocket.api.CloseStatus;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@Getter
public abstract class SocketHandler {
    private static final WebModule module = Module.module(WebModule.class);
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SocketHandler.class);
    private static final List<WsContext> activeSessions = new LinkedList<>();
    private static final Gson gson = GsonBuilder.globalGson();

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

    public void onBinaryMessage(WsBinaryMessageContext binaryMessageContext) throws Exception {
        logger.fine("Received binary message from %s (Id: %s)".formatted(
                binaryMessageContext.session.getLocalAddress(),
                binaryMessageContext.sessionId()
        ));
    }

    public void onConnect(WsConnectContext connectContext) throws Exception {
        logger.fine("Connection established with %s (Id: %s)".formatted(
                connectContext.session.getLocalAddress(),
                connectContext.sessionId()
        ));
        activeSessions.add(connectContext);
    }

    public void onClose(WsCloseContext closeContext) throws Exception {
        logger.fine("Connection closed with %s due to %s (Id: %s)".formatted(
                closeContext.session.getLocalAddress(),
                closeContext.closeStatus().name(),
                closeContext.sessionId()
        ));
    }

    public abstract void onError(WsErrorContext errorContext) throws Exception;

    public final void broadcast(Object object) {
        var data = gson.toJson(object);
        for (WsContext context : List.copyOf(activeSessions)) {
            if (!context.session.isOpen()) {
                logger.info("Removing closed session %s from active sessions".formatted(
                        context.sessionId()
                ));
                activeSessions.remove(context);
                continue;
            }
            context.send(data);
        }
    }

    public static void closeAllSessions() {
        for (WsContext context : List.copyOf(activeSessions)) {
            context.session.close(new CloseStatus(
                    1000,
                    "Server shutting down"
            ));
        }
    }
}