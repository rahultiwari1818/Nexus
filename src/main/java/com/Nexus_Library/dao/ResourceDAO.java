package com.Nexus_Library.dao;

import java.sql.*;
import java.util.*;
import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.Resource;

public class ResourceDAO {
    public List<Resource> getAllResources() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM resources");
        List<Resource> resources = new ArrayList<>();
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
        return resources;
    }
}
