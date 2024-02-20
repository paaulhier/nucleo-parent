package de.keeeks.nucleo.modules.database.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.database.sql.statement.BatchPreparedStatementFiller;
import de.keeeks.nucleo.modules.database.sql.statement.PreparedStatementFiller;
import de.keeeks.nucleo.modules.database.sql.statement.ResultSetTransformer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MysqlConnection {
    private static final List<MysqlConnection> sqlConnections = new ArrayList<>();
    private static final AtomicInteger connectionCounter = new AtomicInteger(1);

    private final Logger logger = Module.module(MysqlDatabaseModule.class).logger();
    private final HikariDataSource hikariDataSource;

    public MysqlConnection(MysqlCredentials sqlCredentials) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:%s://%s:%s/%s".formatted(
                sqlCredentials.type().name().toLowerCase(),
                sqlCredentials.host(),
                sqlCredentials.port(),
                sqlCredentials.database()
        ));
        hikariConfig.setUsername(sqlCredentials.username());
        hikariConfig.setPassword(sqlCredentials.password());
        hikariConfig.setThreadFactory(r -> new Thread(r, "kks-sql-%d"));
        hikariConfig.setPoolName("kks-sql-%d".formatted(
                connectionCounter.getAndIncrement()
        ));
        this.hikariDataSource = new HikariDataSource(hikariConfig);
        sqlConnections.add(this);
    }

    public static void shutdown() {
        sqlConnections.forEach(MysqlConnection::closeSource);
    }

    private void closeSource() {
        hikariDataSource.close();
    }

    public void prepare(String sql) {
        prepare(sql, PreparedStatementFiller.EMPTY);
    }

    public void prepare(String sql, PreparedStatementFiller filler) {
        try (var connection = connection()) {
            var preparedStatement = connection.prepareStatement(sql);
            filler.fill(preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int keyInsert(String sql) {
        return keyInsert(sql, PreparedStatementFiller.EMPTY);
    }

    public int keyInsert(String sql, PreparedStatementFiller filler) {
        return keyInsert(sql, filler, int.class);
    }

    public <T> T keyInsert(String sql, PreparedStatementFiller filler, Class<T> clazz) {
        try (var connection = connection()) {
            var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            filler.fill(preparedStatement);
            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getObject(1, clazz);
            }
        } catch (SQLException e) {
            logger.log(
                    Level.SEVERE,
                    "Error while executing keyInsert: " + e.getMessage(),
                    e
            );
        }
        return null;
    }

    public <T> void batchInsert(
            String sql,
            BatchPreparedStatementFiller<T> filler,
            List<T> list
    ) {
        if (list.isEmpty()) return;
        try (var connection = connection()) {
            var preparedStatement = connection.prepareStatement(sql);
            for (T t : list) {
                filler.fill(t, preparedStatement);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            logger.log(
                    Level.SEVERE,
                    "Error while executing batchInsert: " + e.getMessage(),
                    e
            );
        }
    }

    public <T> List<T> queryList(
            String sql,
            ResultSetTransformer<T> resultSetTransformer
    ) {
        return queryList(sql, PreparedStatementFiller.EMPTY, resultSetTransformer);
    }

    public <T> List<T> queryList(
            String sql,
            PreparedStatementFiller filler,
            ResultSetTransformer<T> resultSetTransformer
    ) {
        List<T> result = new ArrayList<>();

        try (var connection = connection()) {
            var preparedStatement = connection.prepareStatement(sql);
            filler.fill(preparedStatement);
            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result.add(resultSetTransformer.transform(resultSet));
            }

        } catch (SQLException e) {
            logger.log(
                    Level.SEVERE,
                    "Error while executing queryList: " + e.getMessage(),
                    e
            );
        }
        return result;
    }

    public <T> T query(
            String sql,
            ResultSetTransformer<T> resultSetTransformer
    ) {
        return query(sql, PreparedStatementFiller.EMPTY, resultSetTransformer);
    }

    public <T> T query(
            String sql,
            PreparedStatementFiller filler,
            ResultSetTransformer<T> resultSetTransformer
    ) {
        try (var connection = connection()) {
            var preparedStatement = connection.prepareStatement(sql);
            filler.fill(preparedStatement);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSetTransformer.transform(resultSet);
            }
        } catch (SQLException e) {
            logger.log(
                    Level.SEVERE,
                    "Error while executing query: " + e.getMessage(),
                    e
            );
        }
        return null;
    }

    public ResultSet rawExecute(
            String sql,
            PreparedStatementFiller filler
    ) {
        try (var connection = connection()) {
            var preparedStatement = connection.prepareStatement(sql);
            filler.fill(preparedStatement);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            logger.log(
                    Level.SEVERE,
                    "Error while executing rawExecute: " + e.getMessage(),
                    e
            );
        }
        return null;
    }

    public Connection connection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    public static MysqlConnection create(MysqlCredentials sqlCredentials) {
        return new MysqlConnection(sqlCredentials);
    }
}