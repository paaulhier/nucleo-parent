package de.keeeks.nucleo.modules.web.configuration.authorization;

public record ApiKeyAuthenticationConfiguration(
        String apiKey
) {

    public static ApiKeyAuthenticationConfiguration defaultConfiguration() {
        return new ApiKeyAuthenticationConfiguration("default-api");
    }
}