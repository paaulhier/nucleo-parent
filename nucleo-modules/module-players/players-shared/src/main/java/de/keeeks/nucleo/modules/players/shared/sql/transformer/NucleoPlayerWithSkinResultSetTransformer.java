package de.keeeks.nucleo.modules.players.shared.sql.transformer;

import de.keeeks.nucleo.modules.database.sql.statement.ResultSetTransformer;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.Skin;
import de.keeeks.nucleo.modules.players.shared.DefaultNucleoPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class NucleoPlayerWithSkinResultSetTransformer implements ResultSetTransformer<NucleoPlayer> {
    @Override
    public NucleoPlayer transform(ResultSet resultSet) throws SQLException {
        UUID playerId = UUID.fromString(resultSet.getString("uuid"));
        Timestamp lastLogin = resultSet.getTimestamp("lastLogin");
        Timestamp lastLogout = resultSet.getTimestamp("lastLogout");
        return new DefaultNucleoPlayer(
                playerId,
                resultSet.getString("name"),
                new Skin(
                        playerId,
                        resultSet.getString("value"),
                        resultSet.getString("signature")
                ),
                resultSet.getString("lastIpAddress"),
                resultSet.getLong("onlineTime"),
                lastLogin == null ? null : lastLogin.toInstant(),
                lastLogout == null ? null : lastLogout.toInstant(),
                resultSet.getTimestamp("createdAt").toInstant(),
                resultSet.getTimestamp("updatedAt").toInstant()
        );
    }
}