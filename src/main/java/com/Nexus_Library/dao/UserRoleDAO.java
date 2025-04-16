package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.UserRole;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRoleDAO {
    public List<UserRole> getAllUserRoles() throws SQLException {
        List<UserRole> userRoles = new ArrayList<>();
        String query = "SELECT * FROM user_roles";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                userRoles.add(new UserRole(
                        rs.getInt("user_id"),
                        rs.getInt("role_id")
                ));
            }
        }
        return userRoles;
    }
}