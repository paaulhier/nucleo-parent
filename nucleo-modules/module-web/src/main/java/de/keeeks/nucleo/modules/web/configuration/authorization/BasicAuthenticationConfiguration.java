package de.keeeks.nucleo.modules.web.configuration.authorization;

public record BasicAuthenticationConfiguration(
        String username,
        String password
) {

    public static BasicAuthenticationConfiguration defaultConfiguration() {
        return new BasicAuthenticationConfiguration("admin", "admin");
    }
}