package de.keeeks.nucleo.modules.database.redis;

import de.keeeks.nucleo.core.api.Module;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@Getter
public final class RedisConnection {
    private static final List<RedisConnection> connections = new LinkedList<>();

    private final StatefulRedisConnection<String, String> connection;
    private final RedisClient redisClient;

    public RedisConnection(RedisCredentials credentials) {
        this.redisClient = RedisClient.create(buildRedisURI(credentials));
        redisClient.setOptions(ClientOptions.builder()
                .autoReconnect(true)
                .socketOptions(SocketOptions.builder().keepAlive(true).build())
                .build());

        this.connection = redisClient.connect();

        Logger logger = Module.module(RedisDatabaseModule.class).logger();
        logger.info("Created new Redis connection to %s:%d.".formatted(
                credentials.host(),
                credentials.port()
        ));

        connections.add(this);
    }

    private RedisURI buildRedisURI(RedisCredentials credentials) {
        return RedisURI.Builder.redis(
                credentials.host(),
                credentials.port()
        ).withPassword(
                credentials.password().toCharArray()
        ).withDatabase(0).build();
    }

    public void close() {
        connection.close();
        redisClient.close();
    }

    public RedisCommands<String, String> sync() {
        return connection.sync();
    }

    public RedisAsyncCommands<String, String> async() {
        return connection.async();
    }

    public static void closeAll() {
        connections.forEach(RedisConnection::close);
    }
}