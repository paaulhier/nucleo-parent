package de.keeeks.nucleo.modules.database.sql;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;

@ModuleDescription(
        name = "database-mysql",
        description = "Provides a connection to a MySQL database."
)
public class MysqlDatabaseModule extends Module {

    @Override
    public void disable() {
        MysqlConnection.shutdown();
    }
}