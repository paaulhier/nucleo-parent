package de.keeeks.nucleo.modules.players.shared.sql;

import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.players.api.comment.Comment;
import de.keeeks.nucleo.modules.players.shared.sql.transformer.CommentResultSetTransformer;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class CommentRepository {
    private final CommentResultSetTransformer transformer = new CommentResultSetTransformer();

    private final MysqlConnection mysqlConnection;

    public List<Comment> comments(UUID playerId) {
        return mysqlConnection.queryList(
                "select * from comments where playerId = ?",
                statement -> statement.setString(1, playerId.toString()),
                transformer
        );
    }

    public int createComment(Comment comment) {
        return mysqlConnection.keyInsert(
                "insert into comments (id, playerId, creatorId, comment) values (?, ?, ?, ?)",
                statement -> {
                    statement.setString(1, comment.id());
                    statement.setString(2, comment.playerId().toString());
                    statement.setString(3, comment.creatorId().toString());
                    statement.setString(4, comment.content());
                }
        );
    }

    public void updateComment(Comment comment) {
        mysqlConnection.prepare(
                "update comments set comment = ? where id = ?",
                statement -> {
                    statement.setString(1, comment.content());
                    statement.setString(2, comment.id());
                }
        );
    }

    public void deleteComment(Comment comment) {
        mysqlConnection.prepare(
                "delete from comments where id = ?",
                statement -> statement.setString(1, comment.id())
        );
    }
}