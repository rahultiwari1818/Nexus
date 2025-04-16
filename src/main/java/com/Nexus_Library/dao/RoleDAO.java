package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {
    public List<Role> getAllRoles() throws SQLException {
        List<Role> roles = new ArrayList<>();
        String query = "SELECT * FROM roles";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                roles.add(new Role(
                        rs.getInt("role_id"),
                        rs.getString("role_name")
                ));
            }
        }
        return roles;
    }
}

//package com.Nexus_Library.dao;

//import java.sql.*;
//import java.util.*;
//import com.Nexus_Library.config.DBConnection;
//import com.Nexus_Library.model.Role;
//
//public class RoleDAO {
//    public List<Role> getAllRoles() throws SQLException {
//        Connection conn = DBConnection.getConnection();
//        Statement stmt = conn.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT * FROM roles");
//        List<Role> roles = new ArrayList<>();
//        while (rs.next()) {
//            roles.add(new Role(
//                    rs.getInt("role_id"),
//                    rs.getString("role_name")
//            ));
//        }
//        return roles;
//    }
//}
