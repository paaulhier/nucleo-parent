package de.keeeks.nucleo.modules.database.redis;

public record RedisCredentials(
        String host,
        int port,
        String password
) {
}