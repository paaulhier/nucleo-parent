package de.keeeks.nucleo.modules.database.sql.statement;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetTransformer<T> {

    T transform(ResultSet resultSet) throws SQLException;

}