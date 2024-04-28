package de.keeeks.nucleo.modules.players.shared.sql;

import com.google.gson.Gson;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PropertyHolder;
import de.keeeks.nucleo.modules.players.shared.DefaultPropertyHolder;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class PropertiesRepository {
    private final Supplier<Gson> gsonSupplier = GsonBuilder::globalGson;

    private final MysqlConnection mysqlConnection;

    public void deleteProperties(UUID playerId) {
        mysqlConnection.prepare(
                "delete from playerProperties where playerId = ?;",
                statement -> statement.setString(1, playerId.toString())
        );
    }

    public PropertyHolder loadProperties(UUID playerId) {
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

    public void saveProperties(NucleoPlayer player) {
        deleteProperties(player.uuid());

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

}