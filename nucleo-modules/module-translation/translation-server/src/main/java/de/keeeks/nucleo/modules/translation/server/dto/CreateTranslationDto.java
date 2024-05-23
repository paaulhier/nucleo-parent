package de.keeeks.nucleo.modules.translation.server.dto;

public record CreateTranslationDto(
        String key,
        String value,
        String locale,
        int moduleId
) {
}