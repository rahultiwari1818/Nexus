package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.BorrowingSetting;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowingSettingDAO {

    public BorrowingSetting addBorrowingSetting(String userType, int limit, boolean active) throws SQLException {
        String sql = "INSERT INTO borrowing_settings (user_type, borrowing_limit, active) VALUES (?, ?, ?) RETURNING borrowing_setting_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userType);
            stmt.setInt(2, limit);
            stmt.setBoolean(3, active);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("borrowing_setting_id");
                return new BorrowingSetting(id, userType, limit, active);
            }
        }
        return null;
    }

    public boolean updateBorrowingLimit(int settingId, int newLimit) throws SQLException {
        String sql = "UPDATE borrowing_settings SET borrowing_limit = ? WHERE borrowing_setting_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newLimit);
            stmt.setInt(2, settingId);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateActiveStatus(int settingId, boolean newStatus) throws SQLException {
        String sql = "UPDATE borrowing_settings SET active = ? WHERE borrowing_setting_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, newStatus);
            stmt.setInt(2, settingId);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteBorrowingSetting(int settingId) throws SQLException {
        String sql = "DELETE FROM borrowing_settings WHERE borrowing_setting_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, settingId);

            return stmt.executeUpdate() > 0;
        }
    }

    public List<BorrowingSetting> getAllBorrowingSettings() throws SQLException {
        String sql = "SELECT * FROM borrowing_settings";
        List<BorrowingSetting> settings = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BorrowingSetting setting = new BorrowingSetting(
                        rs.getInt("borrowing_setting_id"),
                        rs.getString("user_type"),
                        rs.getInt("borrowing_limit"),
                        rs.getBoolean("active")
                );
                settings.add(setting);
            }
        }

        return settings;
    }
}
