package de.keeeks.nucleo.modules.automessage.shared.sql;

import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;
import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.database.sql.statement.PreparedStatementFiller;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.UUID;

public class AutomaticMessageRepository {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static final AutomaticMessageResultSetTransformer automaticMessageResultSetTransformer = new AutomaticMessageResultSetTransformer(
            miniMessage
    );

    private final MysqlConnection mysqlConnection;

    public AutomaticMessageRepository(MysqlConnection mysqlConnection) {
        this.mysqlConnection = mysqlConnection;
    }

    public void deleteAutomaticMessage(int id) {
        mysqlConnection.prepare(
                "delete from automessages where id = ?;",
                preparedStatement -> preparedStatement.setInt(1, id)
        );
    }

    public List<AutomaticMessage> automaticMessages() {
        return mysqlConnection.queryList(
                "select * from automessages;",
                PreparedStatementFiller.EMPTY,
                automaticMessageResultSetTransformer
        );
    }

    public int insertAutomaticMessage(
            Component message,
            UUID createdBy
    ) {
        return mysqlConnection.keyInsert(
                "insert into automessages (message, createdBy) values (?, ?);",
                preparedStatement -> {
                    preparedStatement.setString(1, miniMessage.serialize(message));
                    preparedStatement.setString(2, createdBy.toString());
                }
        );
    }
}