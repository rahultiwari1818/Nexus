package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.SystemSettings;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SystemSettingsDAO {
    public List<SystemSettings> getAllSystemSettings() throws SQLException {
        List<SystemSettings> settings = new ArrayList<>();
        String query = "SELECT * FROM system_settings";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                settings.add(new SystemSettings(
                        rs.getInt("settings_id"),
                        rs.getInt("max_borrow_limit"),
                        rs.getDouble("fine_rate"),
                        rs.getInt("max_reservation_time"),
                        rs.getString("system_status")
                ));
            }
        }
        return settings;
    }
}

//package com.Nexus_Library.dao;
//
//import java.sql.*;
//import com.Nexus_Library.config.DBConnection;
//import com.Nexus_Library.model.SystemSettings;
//
//public class SystemSettingsDAO {
//    public SystemSettings getSettings() throws SQLException {
//        Connection conn = DBConnection.getConnection();
//        Statement stmt = conn.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT * FROM system_settings LIMIT 1");
//        if (rs.next()) {
//            return new SystemSettings(
//                    rs.getInt("setting_id"),
//                    rs.getInt("max_borrow_limit"),
//                    rs.getDouble("fine_rate"),
//                    rs.getInt("max_reservation_time"),
//                    rs.getString("system_status")
//            );
//        }
//        return null;
//    }
//}
