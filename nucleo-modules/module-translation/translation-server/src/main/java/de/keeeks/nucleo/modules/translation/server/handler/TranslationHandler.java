package de.keeeks.nucleo.modules.translation.server.handler;

import com.google.gson.Gson;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.modules.translation.server.dto.CreateTranslationDto;
import de.keeeks.nucleo.modules.translation.server.dto.UpdateTranslationDto;
import de.keeeks.nucleo.modules.translations.api.TranslationApi;
import de.keeeks.nucleo.modules.translations.api.TranslationEntry;
import de.keeeks.nucleo.modules.web.handler.RequestHandler;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Locale;
import java.util.function.Supplier;

public class TranslationHandler extends RequestHandler {
    private final TranslationApi translationApi = ServiceRegistry.service(TranslationApi.class);
    private final Supplier<Gson> gsonSupplier = GsonBuilder::globalGson;

    public TranslationHandler() {
        super(
                "/",
                HandlerType.GET,
                HandlerType.POST,
                HandlerType.PUT,
                HandlerType.DELETE
        );
    }

    @Override
    public void processRequest(
            Context context,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        if (context.handlerType().equals(HandlerType.GET)) {
            getTranslations(context);
        } else if (context.handlerType().equals(HandlerType.POST)) {
            createTranslation(context);
        } else if (context.handlerType().equals(HandlerType.PUT)) {
            updateTranslation(context);
        } else if (context.handlerType().equals(HandlerType.DELETE)) {
            deleteTranslation(context);
        }
    }

    private void getTranslations(Context context) {
        String localeParam = context.queryParam("locale");
        Locale locale = localeParam == null ? null : TranslationApi.locale(localeParam);

        if (locale == null) {
            context.result(gsonSupplier.get().toJson(translationApi.translations()));
            return;
        }
        context.result(gsonSupplier.get().toJson(translationApi.translations(locale)));
    }

    private void createTranslation(Context context) {
        CreateTranslationDto createTranslationDto = readBody(
                context,
                CreateTranslationDto.class
        );

        translationApi.module(createTranslationDto.moduleId()).ifPresentOrElse(moduleDetails -> {
            Locale locale = TranslationApi.locale(createTranslationDto.locale());
            if (locale == null) {
                writeStatus(context, HttpStatus.BAD_REQUEST, "Invalid locale provided");
                return;
            }

            TranslationEntry translationEntry = translationApi.createTranslationEntry(
                    moduleDetails,
                    locale,
                    createTranslationDto.key(),
                    createTranslationDto.value()
            );
            translationApi.reload();
            writeBody(context, translationEntry);
        }, () -> writeStatus(context, HttpStatus.NO_CONTENT));
    }

    private void updateTranslation(Context context) {
        UpdateTranslationDto updateTranslationDto = readBody(context, UpdateTranslationDto.class);

        translationApi.translationEntry(updateTranslationDto.translationId()).ifPresentOrElse(
                translationEntry -> {
                    boolean changed = false;
                    if (updateTranslationDto.key() != null
                            && !updateTranslationDto.key().equals(translationEntry.key())) {
                        translationEntry.key(updateTranslationDto.key());
                        changed = true;
                    }
                    if (updateTranslationDto.value() != null
                            && !updateTranslationDto.value().equals(translationEntry.value())) {
                        translationEntry.value(updateTranslationDto.value());
                        changed = true;
                    }
                    if (changed) {
                        translationApi.updateTranslationEntry(translationEntry);
                        translationApi.reload();
                    }
                    writeBody(context, translationEntry);
                },
                () -> writeStatus(context, HttpStatus.NO_CONTENT)
        );
    }

    private void deleteTranslation(Context context) {
        String id = context.queryParam("id");
        if (id == null) {
            writeStatus(context, HttpStatus.BAD_REQUEST, "No id provided.");
            return;
        }

        try {
            translationApi.translationEntry(Integer.parseInt(id)).ifPresentOrElse(
                    translationEntry -> {
                        translationApi.deleteTranslationEntry(translationEntry);
                        translationApi.reload();
                        writeStatus(context, HttpStatus.OK);
                    },
                    () -> writeStatus(context, HttpStatus.BAD_REQUEST)
            );
        } catch (NumberFormatException e) {
            writeStatus(context, HttpStatus.BAD_REQUEST, "Id must be a number.");
            return;
        }
    }
}