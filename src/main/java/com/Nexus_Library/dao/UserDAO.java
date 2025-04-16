//package com.Nexus_Library.dao;
//
//import java.sql.*;
//import java.util.*;
//import com.Nexus_Library.config.DBConnection;
//import com.Nexus_Library.model.User;
//
//public class UserDAO {
//    public List<User> getAllUsers() throws SQLException {
//        Connection conn = DBConnection.getConnection();
//        Statement stmt = conn.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
//        List<User> users = new ArrayList<>();
//        while (rs.next()) {
//            users.add(new User(
//                    rs.getInt("user_id"),
//                    rs.getString("first_name"),
//                    rs.getString("last_name"),
//                    rs.getString("email"),
//                    rs.getString("password"),
//                    rs.getTimestamp("registration_date")
//            ));
//        }
//        return users;
//    }
//}
package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getTimestamp("registration_date")
                ));
            }
        }
        return users;
    }
}