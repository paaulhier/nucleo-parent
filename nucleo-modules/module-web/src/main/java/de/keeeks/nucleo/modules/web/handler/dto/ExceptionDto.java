package de.keeeks.nucleo.modules.web.handler.dto;

import java.time.Instant;

public record ExceptionDto(
        String path,
        String message,
        Instant timestamp
) {
}