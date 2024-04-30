package de.keeeks.nucleo.modules.translation.shared.sql.transformer;

import de.keeeks.nucleo.modules.database.sql.statement.ResultSetTransformer;
import de.keeeks.nucleo.modules.translation.shared.DefaultModuleDetails;
import de.keeeks.nucleo.modules.translations.api.ModuleDetails;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModuleDetailsResulSetTransformer implements ResultSetTransformer<ModuleDetails> {
    @Override
    public ModuleDetails transform(ResultSet resultSet) throws SQLException {
        return new DefaultModuleDetails(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getTimestamp("createdAt").toInstant(),
                resultSet.getTimestamp("updatedAt").toInstant()
        );
    }
}