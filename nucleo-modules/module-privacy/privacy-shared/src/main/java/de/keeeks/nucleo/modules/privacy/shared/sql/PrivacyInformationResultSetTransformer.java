package de.keeeks.nucleo.modules.privacy.shared.sql;

import de.keeeks.nucleo.modules.database.sql.statement.ResultSetTransformer;
import de.keeeks.nucleo.modules.privacy.api.PrivacyInformation;
import de.keeeks.nucleo.modules.privacy.shared.NucleoPrivacyInformation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PrivacyInformationResultSetTransformer implements ResultSetTransformer<PrivacyInformation> {
    @Override
    public PrivacyInformation transform(ResultSet resultSet) throws SQLException {
        return new NucleoPrivacyInformation(
                resultSet.getInt("id"),
                UUID.fromString(resultSet.getString("playerId")),
                timestampOrNull(resultSet, "createdAt"),
                resultSet.getBoolean("accepted"),
                timestampOrNull(resultSet, "acceptedAt")
        );
    }
}