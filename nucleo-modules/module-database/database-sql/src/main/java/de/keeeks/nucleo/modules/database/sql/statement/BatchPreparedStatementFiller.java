package de.keeeks.nucleo.modules.database.sql.statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface BatchPreparedStatementFiller<T> {
    void fill(T t, PreparedStatement preparedStatement) throws SQLException;
}