package de.keeeks.nucleo.modules.translation.shared.sql;

import de.keeeks.nucleo.modules.database.sql.statement.ResultSetTransformer;
import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.translation.shared.NucleoEconomy;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class EconomyResultSetTransformer implements ResultSetTransformer<Economy> {
    private final EconomyRepository economyRepository;

    @Override
    public Economy transform(ResultSet resultSet) throws SQLException {
        return new NucleoEconomy(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                economyRepository
        );
    }
}