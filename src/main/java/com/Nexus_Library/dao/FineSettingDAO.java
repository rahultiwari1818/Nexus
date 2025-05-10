package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.FineSetting;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FineSettingDAO {

    public FineSetting addNewFineSetting(String userType, int amt, boolean activeStatus) throws SQLException {
        String insertQuery = "INSERT INTO fine_settings (user_type, fine_amt, active) VALUES (?, ?, ?) RETURNING fine_setting_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, userType);
            stmt.setInt(2, amt);
            stmt.setBoolean(3, activeStatus);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                FineSetting setting = new FineSetting();
                setting.setId(rs.getInt("fine_setting_id"));
                setting.setUserType(userType);
                setting.setFinePerDay(amt);
                setting.setActiveStatus(activeStatus);
                return setting;
            } else {
                System.out.println("⚠️ Failed to insert fine setting.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    public boolean updateFineSettingAmt(int fineSettingId, int newFineAmount) throws SQLException {
        String updateQuery = "UPDATE fine_settings SET fine_amt = ? WHERE fine_setting_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setInt(1, newFineAmount);
            stmt.setInt(2, fineSettingId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean updateFineSettingActiveStatus(int fineSettingId, Boolean newStatus) throws SQLException {
        String updateQuery = "UPDATE fine_settings SET active = ? WHERE fine_setting_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setBoolean(1, newStatus);
            stmt.setInt(2, fineSettingId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean deleteFineSetting(int fineSettingId) throws SQLException {
        String deleteQuery = "DELETE FROM fine_settings WHERE fine_setting_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

            stmt.setInt(1, fineSettingId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public int getFinePerDay(String userType) throws  SQLException{
        String query = "SELECT * FROM fine_settings where user_type = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userType);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("fine_amt");
            } else {
                System.out.println("⚠️ Failed to insert fine setting.");
            }

        }
        return 0;
    }

    public List<FineSetting> getAllFineSetting() throws SQLException {
        String query = "SELECT * FROM fine_settings";
        List<FineSetting> fineSettings = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                FineSetting setting = new FineSetting();
                setting.setId(rs.getInt("fine_setting_id"));
                setting.setUserType(rs.getString("user_type"));
                setting.setFinePerDay(rs.getInt("fine_amt"));
                setting.setActiveStatus(rs.getBoolean("active"));
                fineSettings.add(setting);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return fineSettings;
    }
}
