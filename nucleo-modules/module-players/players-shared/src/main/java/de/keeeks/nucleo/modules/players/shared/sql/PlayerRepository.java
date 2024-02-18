package de.keeeks.nucleo.modules.players.shared.sql;

import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.database.sql.MysqlCredentials;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.shared.DefaultNucleoPlayer;
import de.keeeks.nucleo.modules.players.shared.sql.transformer.NucleoPlayerWithSkinResultSetTransformer;

import java.sql.Timestamp;
import java.util.UUID;


/**
 * The player repository is currently saved under <a href="https://panel.keeeks.de/server/bf00c498/databases">Teamspeak Testserver</a>
 * Don't ask why, the reason is too dumb to explain.
 */

public final class PlayerRepository {
    private static final NucleoPlayerWithSkinResultSetTransformer nucleoPlayerResultSetTransformer = new NucleoPlayerWithSkinResultSetTransformer();

    private final MysqlConnection mysqlConnection;

    public PlayerRepository(MysqlCredentials mysqlCredentials) {
        mysqlConnection = MysqlConnection.create(mysqlCredentials);
    }

    public NucleoPlayer createPlayer(
            UUID uuid,
            String name
    ) {
        mysqlConnection.prepare(
                "insert into players (uuid, name) values (?,?);",
                statement -> {
                    statement.setString(1, uuid.toString());
                    statement.setString(2, name);
                }
        );
        return new DefaultNucleoPlayer(
                uuid,
                name
        );
    }

    public void updatePlayerData(NucleoPlayer player) {
        mysqlConnection.prepare(
                "update players set name = ?, locale = ?, onlineTime = ?, lastLogin = ?, lastLogout = ? where uuid = ?;",
                statement -> {
                    statement.setString(1, player.name());
                    statement.setString(2, player.locale() == null ? "de_DE" : player.locale().toString());
                    statement.setLong(3, player.onlineTime());
                    statement.setTimestamp(
                            4,
                            player.lastLogin() == null ? null : Timestamp.from(player.lastLogin())
                    );
                    statement.setTimestamp(
                            5,
                            player.lastLogout() == null ? null : Timestamp.from(player.lastLogout())
                    );
                    statement.setString(6, player.uuid().toString());
                }
        );
    }

    public void createOrUpdateSkin(UUID playerId, String value, String signature) {
        mysqlConnection.prepare(
                "insert into skins (playerId, value, signature) values (?,?,?) on duplicate key update value=values(value), signature=values(signature)",
                statement -> {
                    statement.setString(1, playerId.toString());
                    statement.setString(2, value);
                    statement.setString(3, signature);
                }
        );
    }

    public NucleoPlayer player(UUID uuid) {
        return mysqlConnection.query(
                "select * from players left join skins s on players.uuid = s.playerId where uuid = ?;",
                statement -> statement.setString(1, uuid.toString()),
                nucleoPlayerResultSetTransformer
        );
    }

    public NucleoPlayer player(String name) {
        return mysqlConnection.query(
                "select * from players left join skins s on players.uuid = s.playerId where uuid = ?;",
                statement -> statement.setString(1, name),
                nucleoPlayerResultSetTransformer
        );
    }
}