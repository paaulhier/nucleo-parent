package de.keeeks.nucleo.modules.web.handler;

public record ResponseEntity<T>(int status, T body) {

    public static <T> ResponseEntity<T> ok(T body) {
        return new ResponseEntity<T>(
                200,
                body
        );
    }

    public static <T> ResponseEntity<T> notFound(T body) {
        return new ResponseEntity<T>(
                404,
                body
        );
    }

    public static <T> ResponseEntity<T> unauthorized(T body) {
        return new ResponseEntity<T>(
                401,
                body
        );
    }

    public static <T> ResponseEntity<T> badRequest(T body) {
        return new ResponseEntity<T>(
                400,
                body
        );
    }

    public static <T> ResponseEntity<T> internalServerError(T body) {
        return new ResponseEntity<T>(
                500,
                body
        );
    }

    public static <T> ResponseEntity<T> of(int status, T body) {
        return new ResponseEntity<T>(
                status,
                body
        );
    }

    public static <T> ResponseEntity<T> of(int status) {
        return new ResponseEntity<T>(
                status,
                null
        );
    }
}