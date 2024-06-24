package de.keeeks.nucleo.modules.players.server.requests;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.web.handler.RequestHandler;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PlayerCountRequestHandler extends RequestHandler {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);

    public PlayerCountRequestHandler() {
        super("/players/count", HandlerType.GET);
    }

    @Override
    public void processRequest(Context context, HttpServletRequest request, HttpServletResponse response) throws Exception {
        writeBody(context, playerService.onlinePlayerCount());
    }
}