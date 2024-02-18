package de.keeeks.nucleo.modules.database.sql.statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementFiller {

    PreparedStatementFiller EMPTY = statement -> {};

    void fill(PreparedStatement statement) throws SQLException;

}