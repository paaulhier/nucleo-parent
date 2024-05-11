package de.keeeks.nucleo.modules.players.shared.sql;

import com.google.gson.Gson;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.database.sql.statement.PreparedStatementFiller;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PropertyHolder;
import de.keeeks.nucleo.modules.players.shared.DefaultNucleoPlayer;
import de.keeeks.nucleo.modules.players.shared.sql.transformer.NucleoPlayerResultSetTransformer;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * The player repository is currently saved under <a href="https://panel.keeeks.de/server/bf00c498/databases">Teamspeak Testserver</a>
 * Don't ask why, the reason is too dumb to explain.
 */

public final class PlayerRepository {
    private final Supplier<Gson> gsonSupplier = GsonBuilder::globalGson;

    private final NucleoPlayerResultSetTransformer playerTransformer;
    private final PropertiesRepository propertiesRepository;
    private final MysqlConnection mysqlConnection;
    private final SkinRepository skinRepository;

    public PlayerRepository(
            MysqlConnection mysqlConnection,
            SkinRepository skinRepository,
            CommentRepository commentRepository,
            PropertiesRepository propertiesRepository
    ) {
        this.mysqlConnection = mysqlConnection;
        this.skinRepository = skinRepository;
        this.propertiesRepository = propertiesRepository;

        this.playerTransformer = new NucleoPlayerResultSetTransformer(commentRepository, skinRepository);
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
        propertiesRepository.saveProperties(player);
    }

    public NucleoPlayer player(UUID uuid) {
        return playerBySql(
                "select * from players where uuid = ?;",
                statement -> statement.setString(1, uuid.toString())
        );
    }

    public NucleoPlayer player(String name) {
        return playerBySql(
                "select * from players where name = ?;",
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
            PropertyHolder propertyHolder = propertiesRepository.loadProperties(nucleoPlayer.uuid());
            nucleoPlayer.properties().setProperties(propertyHolder);
        }
        return nucleoPlayer;
    }

    public List<UUID> playersSortedByPlayTime() {
        return mysqlConnection.queryList(
                "select uuid from players order by onlineTime desc limit 10;",
                resultSet -> UUID.fromString(resultSet.getString("uuid"))
        );
    }

    public void deletePlayer(UUID uuid) {
        skinRepository.deleteSkin(uuid);
        propertiesRepository.deleteProperties(uuid);
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