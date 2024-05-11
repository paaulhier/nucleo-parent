package de.keeeks.nucleo.modules.economy.shared.sql;

import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.economy.shared.NucleoEconomy;

import java.util.List;
import java.util.UUID;

public final class EconomyRepository {
    private final EconomyResultSetTransformer economyResultSetTransformer = new EconomyResultSetTransformer(this);

    private final MysqlConnection mysqlConnection;

    public EconomyRepository(MysqlConnection mysqlConnection) {
        this.mysqlConnection = mysqlConnection;
    }

    public List<Economy> economies() {
        return mysqlConnection.queryList(
                "select * from economies",
                economyResultSetTransformer
        );
    }

    public Economy createEconomy(String name) {
        int economyId = mysqlConnection.keyInsert(
                "insert into economies (name) values (?)",
                statement -> statement.setString(1, name)
        );
        return new NucleoEconomy(economyId, name, this);
    }

    public void deleteEconomy(int id) {
        mysqlConnection.prepare(
                "delete from economies where id = ?",
                statement -> statement.setInt(1, id)
        );
    }

    public double balance(int economyId, UUID uuid) {
        Double balance = mysqlConnection.query(
                "select balance from economy_balances where economyId = ? and playerId = ? and balance > 0;",
                statement -> {
                    statement.setInt(1, economyId);
                    statement.setString(2, uuid.toString());
                },
                resultSet -> resultSet.getDouble("balance")
        );
        if (balance == null) return 0.0;
        return balance;
    }

    public List<UUID> top(int economyId, int amount) {
        return mysqlConnection.queryList(
                "select playerId from economy_balances where economyId = ? order by balance desc limit ?",
                statement -> {
                    statement.setInt(1, economyId);
                    statement.setInt(2, amount);
                },
                resultSet -> UUID.fromString(resultSet.getString("playerId"))
        );
    }

    public void updateBalance(int economyId, UUID uuid, double balance) {
        mysqlConnection.prepare(
                "insert into economy_balances (economyId, playerId, balance) values (?, ?, ?) on duplicate key update balance = values(balance)",
                statement -> {
                    statement.setInt(1, economyId);
                    statement.setString(2, uuid.toString());
                    statement.setDouble(3, balance);
                }
        );
    }
}