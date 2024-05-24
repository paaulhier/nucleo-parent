package de.keeeks.nucleo.modules.translation.server.handler;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.translation.server.dto.LocaleDto;
import de.keeeks.nucleo.modules.translations.api.TranslationApi;
import de.keeeks.nucleo.modules.translations.api.TranslationEntry;
import de.keeeks.nucleo.modules.web.handler.RequestHandler;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LocalesHandler extends RequestHandler {
    private final TranslationApi translationApi = ServiceRegistry.service(TranslationApi.class);

    public LocalesHandler() {
        super("/locale", HandlerType.GET);
    }

    @Override
    public void processRequest(
            Context context,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        writeBody(context, translationApi.translations().stream().map(
                TranslationEntry::locale
        ).distinct().map(locale -> new LocaleDto(
                locale.toLanguageTag(),
                locale.getDisplayName()
        )).toList());
    }
}