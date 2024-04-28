package de.keeeks.nucleo.modules.players.shared.sql.transformer;

import de.keeeks.nucleo.modules.database.sql.statement.ResultSetTransformer;
import de.keeeks.nucleo.modules.players.api.comment.Comment;
import de.keeeks.nucleo.modules.players.shared.comment.NucleoComment;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentResultSetTransformer implements ResultSetTransformer<Comment> {
    @Override
    public Comment transform(ResultSet resultSet) throws SQLException {
        return new NucleoComment(
                resultSet.getString("id"),
                uuidOrNull(resultSet, "playerId"),
                uuidOrNull(resultSet, "creatorId"),
                resultSet.getTimestamp("createdAt").toInstant(),
                resultSet.getString("comment"),
                resultSet.getTimestamp("updatedAt").toInstant()
        );
    }
}