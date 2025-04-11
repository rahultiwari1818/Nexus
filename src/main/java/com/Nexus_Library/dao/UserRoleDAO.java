package com.Nexus_Library.dao;

import java.sql.*;
import java.util.*;
import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.UserRole;

public class UserRoleDAO {
    public List<UserRole> getAllUserRoles() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM user_roles");
        List<UserRole> userRoles = new ArrayList<>();
        while (rs.next()) {
            userRoles.add(new UserRole(rs.getInt("user_id"), rs.getInt("role_id")));
        }
        return userRoles;
    }
}
