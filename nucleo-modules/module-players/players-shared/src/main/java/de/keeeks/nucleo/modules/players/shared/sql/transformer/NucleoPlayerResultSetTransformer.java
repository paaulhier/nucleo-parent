package de.keeeks.nucleo.modules.players.shared.sql.transformer;

import de.keeeks.nucleo.modules.database.sql.statement.ResultSetTransformer;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.shared.DefaultNucleoPlayer;
import de.keeeks.nucleo.modules.players.shared.sql.CommentRepository;
import de.keeeks.nucleo.modules.players.shared.sql.SkinRepository;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

@RequiredArgsConstructor
public class NucleoPlayerResultSetTransformer implements ResultSetTransformer<NucleoPlayer> {
    private final CommentRepository commentRepository;
    private final SkinRepository skinRepository;

    @Override
    public NucleoPlayer transform(ResultSet resultSet) throws SQLException {
        UUID playerId = UUID.fromString(resultSet.getString("uuid"));
        Timestamp lastLogin = resultSet.getTimestamp("lastLogin");
        Timestamp lastLogout = resultSet.getTimestamp("lastLogout");
        return new DefaultNucleoPlayer(
                playerId,
                resultSet.getString("name"),
                skinRepository.skin(playerId),
                resultSet.getString("lastIpAddress"),
                resultSet.getLong("onlineTime"),
                commentRepository.comments(playerId),
                lastLogin == null ? null : lastLogin.toInstant(),
                lastLogout == null ? null : lastLogout.toInstant(),
                resultSet.getTimestamp("createdAt").toInstant(),
                resultSet.getTimestamp("updatedAt").toInstant()
        );
    }
}