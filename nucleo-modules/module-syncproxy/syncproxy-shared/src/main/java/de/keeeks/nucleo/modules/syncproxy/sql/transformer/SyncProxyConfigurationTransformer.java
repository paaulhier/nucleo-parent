package de.keeeks.nucleo.modules.syncproxy.sql.transformer;

import de.keeeks.nucleo.modules.database.sql.statement.ResultSetTransformer;
import de.keeeks.nucleo.syncproxy.api.SyncProxyConfiguration;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SyncProxyConfigurationTransformer implements ResultSetTransformer<SyncProxyConfiguration> {
    @Override
    public SyncProxyConfiguration transform(ResultSet resultSet) throws SQLException {
        return new SyncProxyConfiguration(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getBoolean("maintenance"),
                resultSet.getBoolean("active"),
                resultSet.getString("protocolText"),
                resultSet.getInt("maxPlayers"),
                resultSet.getTimestamp("createdAt").toInstant(),
                resultSet.getTimestamp("updatedAt").toInstant()
        );
    }
}