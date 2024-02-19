package de.keeeks.nucleo.modules.syncproxy.sql.transformer;

import de.keeeks.nucleo.modules.database.sql.statement.ResultSetTransformer;
import de.keeeks.nucleo.syncproxy.api.configuration.MotdConfiguration;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class MotdConfigurationTransformer implements ResultSetTransformer<MotdConfiguration> {
    private final MiniMessage miniMessage;

    @Override
    public MotdConfiguration transform(ResultSet resultSet) throws SQLException {
        return new MotdConfiguration(
                resultSet.getInt("id"),
                resultSet.getInt("configurationId"),
                miniMessage.deserialize(
                        resultSet.getString("firstLine")
                ),
                miniMessage.deserialize(
                        resultSet.getString("secondLine")
                ),
                resultSet.getTimestamp("createdAt").toInstant(),
                resultSet.getTimestamp("updatedAt").toInstant()
        );
    }
}