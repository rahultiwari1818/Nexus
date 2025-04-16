package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAO {
    public List<Resource> getAllResources() throws SQLException {
        List<Resource> resources = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            if (conn.isClosed()) {
                throw new SQLException("Connection is closed before use.");
            }
            String query = "SELECT * FROM resources";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                resources.add(new Resource(
                        rs.getInt("resource_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publisher"),
                        rs.getString("isbn"),
                        rs.getString("resource_type"),
                        rs.getString("status"),
                        rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Database error in ResourceDAO: " + e.getMessage());
            throw e;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null && !conn.isClosed()) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return resources;
    }
}