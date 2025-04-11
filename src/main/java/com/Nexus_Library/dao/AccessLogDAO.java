package com.Nexus_Library.dao;

import java.sql.*;
import java.util.*;
import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.AccessLog;

public class AccessLogDAO {
    public List<AccessLog> getAllAccessLogs() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM access_log");
        List<AccessLog> logs = new ArrayList<>();
        while (rs.next()) {
            logs.add(new AccessLog(
                    rs.getInt("access_log_id"),
                    rs.getInt("user_id"),
                    rs.getInt("resource_id"),
                    rs.getTimestamp("access_time"),
                    rs.getString("access_type")
            ));
        }
        return logs;
    }
}
