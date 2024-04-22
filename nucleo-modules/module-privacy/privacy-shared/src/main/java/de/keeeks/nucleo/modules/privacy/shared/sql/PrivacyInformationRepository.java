package de.keeeks.nucleo.modules.privacy.shared.sql;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.database.sql.MysqlCredentials;
import de.keeeks.nucleo.modules.privacy.api.PrivacyInformation;
import de.keeeks.nucleo.modules.privacy.shared.NucleoPrivacyInformation;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class PrivacyInformationRepository {
    private final PrivacyInformationResultSetTransformer privacyInformationResultSetTransformer = new PrivacyInformationResultSetTransformer();

    private final MysqlConnection mysqlConnection;

    public PrivacyInformationRepository(Module module) {
        mysqlConnection = MysqlConnection.create(JsonConfiguration.create(module.dataFolder(), "mysql").loadObject(
                MysqlCredentials.class,
                MysqlCredentials.defaultCredentials()
        ));
    }

    public PrivacyInformation create(UUID uuid) {
        int id = mysqlConnection.keyInsert(
                "insert into privacy (playerId) values (?)",
                statement -> statement.setString(1, uuid.toString())
        );
        System.out.println("Created privacy information for " + uuid + " with id " + id);

        return new NucleoPrivacyInformation(
                id,
                uuid,
                Instant.now()
        );
    }

    public PrivacyInformation privacyInformation(UUID uuid) {
        return mysqlConnection.query(
                "select * from privacy where playerId = ?",
                statement -> statement.setString(1, uuid.toString()),
                privacyInformationResultSetTransformer
        );
    }

    public void accept(PrivacyInformation privacyInformation) {
        mysqlConnection.prepare(
                "update privacy set accepted = ?, acceptedAt = ? where id = ?",
                statement -> {
                    statement.setBoolean(1, true);
                    statement.setTimestamp(2, Timestamp.from(Instant.now()));
                    statement.setInt(3, privacyInformation.id());
                }
        );
    }
}