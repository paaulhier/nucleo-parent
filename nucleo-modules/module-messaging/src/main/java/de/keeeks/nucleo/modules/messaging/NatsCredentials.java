package de.keeeks.nucleo.modules.messaging;

public record NatsCredentials(
        String hostname,
        int port,
        String username,
        String password
) {

    public static NatsCredentials createDefault() {
        return new NatsCredentials(
                "127.0.0.1",
                4222,
                "username",
                "port"
        );
    }
}