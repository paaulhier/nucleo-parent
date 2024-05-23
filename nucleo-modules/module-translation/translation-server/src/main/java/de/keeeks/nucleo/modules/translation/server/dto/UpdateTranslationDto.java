package de.keeeks.nucleo.modules.translation.server.dto;

public record UpdateTranslationDto(
        int translationId,
        String key,
        String value
) {
}