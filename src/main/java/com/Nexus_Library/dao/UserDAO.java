package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.User;
import com.Nexus_Library.pattern.creational.UserFactory;

import java.sql.*;
import java.util.*;

public class UserDAO {
    public boolean registerUser(User user) throws SQLException {
        String query = "INSERT INTO users (first_name, last_name, email, password, registration_date, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setTimestamp(5, user.getRegistrationDate());
            stmt.setString(6, user.getRole());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
        }
    }

    public User loginUser(String email, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return UserFactory.createUser(rs.getString("role"), rs.getInt("user_id"), rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("email"), rs.getString("password"), rs.getTimestamp("registration_date")
                );
            }
            return null;
        }
    }

    public List<User> getAllUsers(String searchQuery) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role <> ? AND (" +
                "CAST(user_id AS TEXT) ILIKE ? OR " +
                "first_name ILIKE ? OR " +
                "last_name ILIKE ? OR " +
                "email ILIKE ? OR " +
                "password ILIKE ? OR " +
                "CAST(registration_date AS TEXT) ILIKE ?" +
                ")";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String likeQuery = "%" + searchQuery + "%";
            stmt.setString(1, "Admin");
            stmt.setString(2, likeQuery); // user_id
            stmt.setString(3, likeQuery); // first_name
            stmt.setString(4, likeQuery); // last_name
            stmt.setString(5, likeQuery); // email
            stmt.setString(6, likeQuery); // password
            stmt.setString(7, likeQuery); // registration_date

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(UserFactory.createUser(
                            rs.getString("role"),
                            rs.getInt("user_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getTimestamp("registration_date")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

    public User updateProfile(User user) {
        String query = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ? WHERE user_id = ? RETURNING user_id, user_type_id, first_name, last_name, email, password, registration_date";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the parameters for the update
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setInt(5, user.getUserId());

            // Execute the update and get the result
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Create and return the updated User object
                return UserFactory.createUser(
                        rs.getString("role"),
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getTimestamp("registration_date")
                );
            } else {
                System.err.println("No user found with user_id: " + user.getUserId());
                return null;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during profile update: " + e.getMessage());
            return null;
        }
    }

}