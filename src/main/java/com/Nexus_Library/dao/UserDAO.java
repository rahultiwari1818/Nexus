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

    public List<User> searchByEmail(String emailQuery) throws SQLException {
        String sql = "SELECT * FROM users WHERE email ILIKE ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + emailQuery + "%");
            return executeUserQuery(stmt);
        }
    }

    public List<User> searchByName(String nameQuery) throws SQLException {
        String sql = "SELECT * FROM users WHERE first_name ILIKE ? OR last_name ILIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nameQuery + "%");
            stmt.setString(2, "%" + nameQuery + "%");
            return executeUserQuery(stmt);
        }
    }

    public List<User> searchByRole(String roleQuery) throws SQLException {
        String sql = "SELECT * FROM users WHERE role ILIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + roleQuery + "%");
            return executeUserQuery(stmt);
        }
    }

    private List<User> executeUserQuery(PreparedStatement stmt) throws SQLException {
        List<User> users = new ArrayList<>();
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
        return users;
    }

//    ---------------------------------------------------------------------------------------------------------

//    ---- Update Profile ------------------------------------------------------------------------

    public User updateProfile(User user) throws SQLException {
        String query = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ? WHERE user_id = ? RETURNING user_id, first_name, last_name, email, password, registration_date,role";

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