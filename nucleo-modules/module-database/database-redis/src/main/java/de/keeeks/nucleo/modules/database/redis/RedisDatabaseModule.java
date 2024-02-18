package de.keeeks.nucleo.modules.database.redis;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;

@ModuleDescription(
        name = "database-redis",
        description = "Provides a connection to a Redis database."
)
public class RedisDatabaseModule extends Module {
    @Override
    public void disable() {
        RedisConnection.closeAll();
    }
}