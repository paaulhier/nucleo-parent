package de.keeeks.nucleo.modules.database.sql.statement;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;

public interface ResultSetTransformer<T> {

    T transform(ResultSet resultSet) throws SQLException;

    @Nullable
    default UUID uuidOrNull(ResultSet resultSet, String key) throws SQLException {
        return resultSet.getString(key) != null ? UUID.fromString(resultSet.getString(key)) : null;
    }

    @Nullable
    default Instant timestampOrNull(ResultSet resultSet, String key) throws SQLException {
        return resultSet.getTimestamp(key) != null ? resultSet.getTimestamp(key).toInstant() : null;
    }
}