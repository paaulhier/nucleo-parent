package de.keeeks.nucleo.modules.web.handler.dto;

public record StatusDto<T>(
        String path,
        int status,
        T data
) {
}