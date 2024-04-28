package de.keeeks.nucleo.modules.players.shared.sql;

import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.players.api.Skin;
import de.keeeks.nucleo.modules.players.shared.sql.transformer.SkinResultSetTransformer;

import java.util.UUID;

public class SkinRepository {
    private static final SkinResultSetTransformer transformer = new SkinResultSetTransformer();

    private final MysqlConnection mysqlConnection;

    public SkinRepository(MysqlConnection mysqlConnection) {
        this.mysqlConnection = mysqlConnection;
    }

    public Skin skin(UUID playerId) {
        return mysqlConnection.query(
                "select * from skins where playerId = ?;",
                statement -> statement.setString(1, playerId.toString()),
                transformer
        );
    }

    public void createOrUpdateSkin(UUID uuid, String value, String signature) {
        mysqlConnection.prepare(
                "insert into skins (playerId, value, signature) values (?,?,?)" +
                        " on duplicate key update value=values(value), signature=values(signature);",
                statement -> {
                    statement.setString(1, uuid.toString());
                    statement.setString(2, value);
                    statement.setString(3, signature);
                }
        );
    }

    public void deleteSkin(UUID uuid) {
        mysqlConnection.prepare(
                "delete from skins where uuid = ?;",
                statement -> statement.setString(1, uuid.toString())
        );
    }
}