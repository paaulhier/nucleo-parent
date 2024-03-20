package de.keeeks.nucleo.modules.automessage.shared.sql;

import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;
import de.keeeks.nucleo.modules.automessage.shared.NucleoAutomaticMessage;
import de.keeeks.nucleo.modules.database.sql.statement.ResultSetTransformer;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@RequiredArgsConstructor
public class AutomaticMessageResultSetTransformer implements ResultSetTransformer<AutomaticMessage> {
    private final MiniMessage miniMessage;

    @Override
    public AutomaticMessage transform(ResultSet resultSet) throws SQLException {
        return new NucleoAutomaticMessage(
                resultSet.getInt("id"),
                resultSet.getTimestamp("createdAt").toInstant(),
                UUID.fromString(resultSet.getString("createdBy")),
                miniMessage.deserialize(resultSet.getString("message")),
                timestampOrNull(resultSet, "updatedAt"),
                uuidOrNull(resultSet, "updatedBy"),
                resultSet.getBoolean("enabled")
        );
    }
}