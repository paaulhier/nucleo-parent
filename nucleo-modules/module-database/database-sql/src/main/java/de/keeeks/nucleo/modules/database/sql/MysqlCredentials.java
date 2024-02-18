package de.keeeks.nucleo.modules.database.sql;

public record MysqlCredentials(
        String host,
        int port,
        String database,
        String username,
        String password,
        MysqlDatabaseType type
) {

    public static MysqlCredentials defaultCredentials() {
        return new MysqlCredentials(
                "localhost",
                3306,
                "nucleo",
                "root",
                "root",
                MysqlDatabaseType.MYSQL
        );
    }
}