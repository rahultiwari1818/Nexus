package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.AuditTrail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuditTrailDAO {
    public List<AuditTrail> getAllAuditTrails() throws SQLException {
        List<AuditTrail> auditTrails = new ArrayList<>();
        String query = "SELECT * FROM audit_trails";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                auditTrails.add(new AuditTrail(
                        rs.getInt("audit_trail_id"),
                        rs.getInt("resource_id"),
                        rs.getInt("user_id"),
                        rs.getString("action_type"),
                        rs.getTimestamp("timestamp")
                ));
            }
        }
        return auditTrails;
    }
}

//package com.Nexus_Library.dao;
//
//import java.sql.*;
//import java.util.*;
//import com.Nexus_Library.config.DBConnection;
//import com.Nexus_Library.model.AuditTrail;
//
//public class AuditTrailDAO {
//    public List<AuditTrail> getAllAuditTrails() throws SQLException {
//        Connection conn = DBConnection.getConnection();
//        Statement stmt = conn.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT * FROM audit_trails");
//        List<AuditTrail> trails = new ArrayList<>();
//        while (rs.next()) {
//            trails.add(new AuditTrail(
//                    rs.getInt("audit_id"),
//                    rs.getInt("resource_id"),
//                    rs.getInt("user_id"),
//                    rs.getString("action_type"),
//                    rs.getTimestamp("timestamp")
//            ));
//        }
//        return trails;
//    }
//}
