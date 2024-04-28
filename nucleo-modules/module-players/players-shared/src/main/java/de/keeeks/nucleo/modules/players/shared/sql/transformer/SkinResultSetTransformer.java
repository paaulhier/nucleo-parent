package de.keeeks.nucleo.modules.players.shared.sql.transformer;

import de.keeeks.nucleo.modules.database.sql.statement.ResultSetTransformer;
import de.keeeks.nucleo.modules.players.api.Skin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SkinResultSetTransformer implements ResultSetTransformer<Skin> {
    @Override
    public Skin transform(ResultSet resultSet) throws SQLException {
        return new Skin(
                UUID.fromString(resultSet.getString("playerId")),
                resultSet.getString("value"),
                resultSet.getString("signature")
        );
    }
}