package com.Nexus_Library.dao;

import java.sql.*;
import java.util.*;
import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.Role;

public class RoleDAO {
    public List<Role> getAllRoles() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM roles");
        List<Role> roles = new ArrayList<>();
        while (rs.next()) {
            roles.add(new Role(
                    rs.getInt("role_id"),
                    rs.getString("role_name")
            ));
        }
        return roles;
    }
}
