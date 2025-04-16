package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.Notification;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    public List<Notification> getAllNotifications() throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notifications";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                notifications.add(new Notification(
                        rs.getInt("notification_id"),
                        rs.getInt("user_id"),
                        rs.getString("notification_type"),
                        rs.getString("message"),
                        rs.getTimestamp("sent_at"),
                        rs.getString("status"),
                        rs.getString("delivery_method")
                ));
            }
        }
        return notifications;
    }
}
//package com.Nexus_Library.dao;
//
//import com.Nexus_Library.config.DBConnection;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class NotificationDAO {
//    public List<String> getUserNotifications(int userId) throws SQLException {
//        List<String> messages = new ArrayList<>();
//        String query = "SELECT message FROM notifications WHERE user_id = ? AND status = 'Pending'";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setInt(1, userId);
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    messages.add(rs.getString("message"));
//                }
//            }
//        }
//        return messages;
//    }
//
//    public void addNotification(int userId, String type, String message) throws SQLException {
//        String query = "INSERT INTO notifications (user_id, notification_type, message, delivery_method) VALUES (?, ?, ?, 'Console')";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setInt(1, userId);
//            stmt.setString(2, type);
//            stmt.setString(3, message);
//            stmt.executeUpdate();
//        }
//    }
//}