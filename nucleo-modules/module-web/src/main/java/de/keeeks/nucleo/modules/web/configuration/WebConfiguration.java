package de.keeeks.nucleo.modules.web.configuration;

public record WebConfiguration(
        String host,
        int port,
        boolean devLogging,
        AuthenticationType authenticationType
) {

    public static WebConfiguration defaultConfiguration() {
        return new WebConfiguration("localhost", 8080, true, AuthenticationType.NONE);
    }

    public enum AuthenticationType {
        NONE,
        BASIC,
        API_KEY,
        CUSTOM
    }
}