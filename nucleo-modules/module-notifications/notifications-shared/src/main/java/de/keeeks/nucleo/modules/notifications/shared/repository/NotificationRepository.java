package de.keeeks.nucleo.modules.notifications.shared.repository;

import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public final class NotificationRepository {
    private static final NotificationResultSetTransformer notificationResultSetTransformer = new NotificationResultSetTransformer();

    private final MysqlConnection mysqlConnection;

    public int createNotification(String name, String description, String requiredPermission) {
        return mysqlConnection.keyInsert(
                "insert into notifications (name, description, requiredPermission) values (?, ?, ?)",
                statement -> {
                    statement.setString(1, name);
                    statement.setString(2, description);
                    statement.setString(3, requiredPermission);
                }
        );
    }

    public void updateNotification(
            Notification notification
    ) {
        mysqlConnection.prepare(
                "update notifications set name = ?, description = ? where id = ?",
                statement -> {
                    statement.setString(1, notification.name());
                    statement.setString(2, notification.description());
                    statement.setInt(3, notification.id());
                }
        );
    }

    public void deleteNotification(int id) {
        mysqlConnection.prepare("delete from notifications where id = ?", statement -> {
            statement.setInt(1, id);
        });
    }

    public List<Notification> notifications() {
        return mysqlConnection.queryList(
                "select * from notifications",
                notificationResultSetTransformer
        );
    }

    public void notificationState(
            int notificationId,
            UUID uuid,
            boolean active
    ) {
        mysqlConnection.prepare(
                "insert into notificationState (notificationId, uuid, state) values (?, ?, ?) on duplicate key update state = values(state)",
                statement -> {
                    statement.setInt(1, notificationId);
                    statement.setString(2, uuid.toString());
                    statement.setBoolean(3, active);
                }
        );
    }

    public boolean notificationState(
            int notificationId,
            UUID uuid
    ) {
        Boolean active = mysqlConnection.query(
                "select state from notificationState where notificationId = ? and uuid = ?",
                statement -> {
                    statement.setInt(1, notificationId);
                    statement.setString(2, uuid.toString());
                },
                resultSet -> resultSet.getBoolean("state")
        );
        if (active == null) return false;
        return active;
    }
}