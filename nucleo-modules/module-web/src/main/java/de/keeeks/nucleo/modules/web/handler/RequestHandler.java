package de.keeeks.nucleo.modules.web.handler;

import com.google.gson.Gson;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.modules.web.WebModule;
import de.keeeks.nucleo.modules.web.handler.dto.ExceptionDto;
import de.keeeks.nucleo.modules.web.handler.dto.StatusDto;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public abstract class RequestHandler implements Handler {
    protected final Logger logger = Module.module(WebModule.class).logger();

    protected final Supplier<Gson> gson = GsonBuilder::globalGson;

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
            context.async(() -> {
                try {
                    processRequest(context, context.req(), context.res());
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

    protected final <T> T readBody(Context context, Class<T> clazz) {
        return gson.get().fromJson(context.body(), clazz);
    }

    protected final <T> void writeBody(Context context, T t) {
        writeBody(context, HttpStatus.OK, t);
    }

    protected final <T> void writeBody(Context context, int httpStatus, T t) {
        context.status(httpStatus);
        context.result(gson.get().toJson(t));
    }

    protected final <T> void writeBody(Context context, HttpStatus httpStatus, T t) {
        writeBody(context, httpStatus.getCode(), t);
    }

    protected final void writeError(Context context, HttpStatus httpStatus, Throwable throwable) {
        context.status(httpStatus);
        context.result(gson.get().toJson(new ExceptionDto(
                context.path(),
                throwable.getMessage(),
                Instant.now()
        )));
    }

    protected final <T> void writeStatus(Context context, HttpStatus httpStatus, T data) {
        context.status(httpStatus);
        context.result(gson.get().toJson(new StatusDto<>(
                context.path(),
                httpStatus.getCode(),
                data
        )));
    }

    protected final void writeStatus(Context context, HttpStatus httpStatus) {
        writeStatus(context, httpStatus, null);
    }

    protected final <T> void sendResponse(
            HttpServletResponse response,
            ResponseEntity<T> responseEntity
    ) throws Exception {
        sendResponse(
                response,
                responseEntity.status(),
                responseEntity.body()
        );
    }

    protected final void sendResponse(
            HttpServletResponse response,
            int status,
            Object o
    ) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(gson.get().toJson(o));
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"An error occurred while sending a response\"}");
            logger.log(Level.SEVERE, "An error occurred while sending a response", e);
        }
    }

    public abstract void processRequest(
            Context context,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception;

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