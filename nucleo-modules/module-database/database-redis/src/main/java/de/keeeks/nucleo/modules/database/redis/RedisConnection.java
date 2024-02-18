package de.keeeks.nucleo.modules.database.redis;

import de.keeeks.nucleo.core.api.Module;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@Getter
@Accessors(fluent = true)
public final class RedisConnection {
    private static final List<RedisConnection> connections = new LinkedList<>();

    private final RedisAsyncCommands<String, String> async;
    private final RedisCommands<String, String> sync;

    public RedisConnection(RedisCredentials credentials) {
        try (RedisClient redisClient = RedisClient.create(RedisURI.create(
                "redis://%s:%d".formatted(
                        credentials.host(),
                        credentials.port()
                )
        ))) {
            StatefulRedisConnection<String, String> connect = redisClient.connect();

            async = connect.async();
            sync = connect.sync();

            Logger logger = Module.module(RedisDatabaseModule.class).logger();
            logger.info("Created new Redis connection to %s:%d.".formatted(
                    credentials.host(),
                    credentials.port()
            ));

            if (credentials.password() != null) {
                sync.auth(credentials.password());
                async.auth(credentials.password()).whenComplete(
                        (s, throwable) -> logger.info("Authenticated to Redis database.")
                );
            }
        }
        connections.add(this);
    }

    public void close() {
        sync.quit();
        async.quit();
    }

    public static void closeAll() {
        connections.forEach(RedisConnection::close);
    }
}