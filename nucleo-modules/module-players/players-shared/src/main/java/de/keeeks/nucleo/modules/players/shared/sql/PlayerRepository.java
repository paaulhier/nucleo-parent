package de.keeeks.nucleo.modules.players.shared.sql;

import com.google.gson.Gson;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.database.sql.MysqlCredentials;
import de.keeeks.nucleo.modules.database.sql.statement.PreparedStatementFiller;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PropertyHolder;
import de.keeeks.nucleo.modules.players.shared.DefaultNucleoPlayer;
import de.keeeks.nucleo.modules.players.shared.DefaultPropertyHolder;
import de.keeeks.nucleo.modules.players.shared.sql.transformer.NucleoPlayerWithSkinResultSetTransformer;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Supplier;


/**
 * The player repository is currently saved under <a href="https://panel.keeeks.de/server/bf00c498/databases">Teamspeak Testserver</a>
 * Don't ask why, the reason is too dumb to explain.
 */

public final class PlayerRepository {
    private static final NucleoPlayerWithSkinResultSetTransformer playerTransformer = new NucleoPlayerWithSkinResultSetTransformer();

    private final Supplier<Gson> gsonSupplier = GsonBuilder::globalGson;
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
                "update players set name = ?, lastIpAddress = ?, onlineTime = ?, lastLogin = ?, lastLogout = ? where uuid = ?;",
                statement -> {
                    statement.setString(1, player.name());
                    statement.setString(2, player.lastIpAddress());
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
        saveProperties(player);
    }

    public void saveProperties(NucleoPlayer player) {
        mysqlConnection.prepare(
                "delete from playerProperties where playerId = ?;",
                statement -> statement.setString(1, player.uuid().toString())
        );

        StringBuilder stringBuilder = new StringBuilder("insert into playerProperties (playerId, propertyKey, propertyValue) values ");
        Collection<String> keys = player.properties().keys();
        if (keys.isEmpty()) return;
        for (String ignored : keys) {
            stringBuilder.append("(?, ?, ?),");
        }

        mysqlConnection.prepare(
                stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString(),
                statement -> {
                    for (String key : keys) {
                        statement.setString(1, player.uuid().toString());
                        statement.setString(2, key);
                        statement.setString(
                                3,
                                gsonSupplier.get().toJson((Object) player.properties().property(key))
                        );
                        statement.addBatch();
                    }
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
        return playerBySql(
                "select * from players left join skins s on players.uuid = s.playerId where uuid = ?;",
                statement -> statement.setString(1, uuid.toString())
        );
    }

    public NucleoPlayer player(String name) {
        return playerBySql(
                "select * from players left join skins s on players.uuid = s.playerId where name = ?;",
                statement -> statement.setString(1, name)
        );
    }

    private NucleoPlayer playerBySql(String sql, PreparedStatementFiller preparedStatementFiller) {
        NucleoPlayer nucleoPlayer = mysqlConnection.query(
                sql,
                preparedStatementFiller,
                playerTransformer
        );

        if (nucleoPlayer != null) {
            PropertyHolder propertyHolder = loadProperties(nucleoPlayer.uuid());
            nucleoPlayer.properties().setProperties(propertyHolder);
        }
        return nucleoPlayer;
    }

    private PropertyHolder loadProperties(UUID playerId) {
        PropertyHolder propertyHolder = new DefaultPropertyHolder();
        mysqlConnection.queryList(
                "select * from playerProperties where playerId = ?;",
                statement -> statement.setString(1, playerId.toString()),
                resultSet -> propertyHolder.setProperty(
                        resultSet.getString("propertyKey"),
                        gsonSupplier.get().fromJson(
                                resultSet.getString("propertyValue"),
                                Object.class
                        )
                )
        );
        return propertyHolder;
    }

    public void deletePlayer(UUID uuid) {
        mysqlConnection.prepare(
                "delete from skins where playerId = ?;",
                statement -> statement.setString(1, uuid.toString())
        );
        mysqlConnection.prepare(
                "delete from playerProperties where playerId = ?;",
                statement -> statement.setString(1, uuid.toString())
        );
        mysqlConnection.prepare(
                "delete from players where uuid = ?;",
                statement -> statement.setString(1, uuid.toString())
        );
    }

    public Collection<? extends NucleoPlayer> players(String ipAddress) {
        return mysqlConnection.queryList(
                "select uuid from players where lastIpAddress = ?;",
                statement -> statement.setString(1, ipAddress),
                resultSet -> player(UUID.fromString(resultSet.getString("uuid")))
        );
    }
}