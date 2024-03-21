package de.keeeks.nucleo.modules.notifications.shared.repository;

import de.keeeks.nucleo.modules.database.sql.statement.ResultSetTransformer;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import de.keeeks.nucleo.modules.notifications.shared.NucleoNotification;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class NotificationResultSetTransformer implements ResultSetTransformer<Notification> {
    @Override
    public Notification transform(ResultSet resultSet) throws SQLException {
        return new NucleoNotification(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getTimestamp("createdAt").toInstant(),
                resultSet.getTimestamp("updatedAt").toInstant()
        );
    }
}