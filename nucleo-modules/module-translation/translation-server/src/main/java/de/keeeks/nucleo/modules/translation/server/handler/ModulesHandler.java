package de.keeeks.nucleo.modules.translation.server.handler;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.translations.api.TranslationApi;
import de.keeeks.nucleo.modules.web.handler.RequestHandler;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ModulesHandler extends RequestHandler {
    private final TranslationApi translationApi = ServiceRegistry.service(TranslationApi.class);

    public ModulesHandler() {
        super("/modules", HandlerType.GET);
    }

    @Override
    public void processRequest(
            Context context,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        String moduleId = context.queryParam("id");
        if (moduleId != null) {
            try {
                translationApi.module(Integer.parseInt(moduleId)).ifPresentOrElse(
                        moduleDetails -> writeBody(context, moduleDetails),
                        () -> writeStatus(context, HttpStatus.NO_CONTENT)
                );
            } catch (NumberFormatException exception) {
                writeStatus(context, HttpStatus.BAD_REQUEST, "Invalid moduleId provided.");
            }
            return;
        }

        writeBody(context, translationApi.modules());
    }
}