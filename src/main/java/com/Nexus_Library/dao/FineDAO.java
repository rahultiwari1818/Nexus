package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.Fine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FineDAO {

    // Create a new fine record
    public boolean createFine(Fine fine) throws SQLException {
        String query = "INSERT INTO fines (transaction_id, user_id, fine_amount, fine_calculated_date, payment_status) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING fine_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, fine.getTransactionId());
            stmt.setInt(2, fine.getUserId());
            stmt.setDouble(3, fine.getFineAmount());
            stmt.setTimestamp(4, fine.getFineCalculatedDate());
            stmt.setString(5, fine.getPaymentStatus());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                fine.setFineId(rs.getInt("fine_id"));
                return true;
            }
            return false;
        }
    }

    // Retrieve a fine by fine_id
    public Fine getFineById(int fineId) throws SQLException {
        String query = "SELECT * FROM fines WHERE fine_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, fineId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Fine(
                        rs.getInt("fine_id"),
                        rs.getInt("transaction_id"),
                        rs.getInt("user_id"),
                        rs.getDouble("fine_amount"),
                        rs.getTimestamp("fine_calculated_date"),
                        rs.getString("payment_status"),
                        rs.getTimestamp("payment_date"),
                        rs.getObject("waived_by", Integer.class), // Nullable
                        rs.getString("waived_reason") // Nullable
                );
            }
            return null;
        }
    }

    // Retrieve all fines for a user
    public List<Fine> getFinesByUser(int userId) throws SQLException {
        List<Fine> fines = new ArrayList<>();
        String query = "SELECT * FROM fines WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Fine fine = new Fine(
                        rs.getInt("fine_id"),
                        rs.getInt("transaction_id"),
                        rs.getInt("user_id"),
                        rs.getDouble("fine_amount"),
                        rs.getTimestamp("fine_calculated_date"),
                        rs.getString("payment_status"),
                        rs.getTimestamp("payment_date"),
                        rs.getObject("waived_by", Integer.class),
                        rs.getString("waived_reason")
                );
                fines.add(fine);
            }
        }
        return fines;
    }

    // Update fine payment status (e.g., mark as Paid)
    public boolean updateFinePayment(int fineId, String paymentStatus, Timestamp paymentDate) throws SQLException {
        String query = "UPDATE fines SET payment_status = ?, payment_date = ? WHERE fine_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, paymentStatus);
            stmt.setTimestamp(2, paymentDate);
            stmt.setInt(3, fineId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Waive a fine (set payment_status to 'Waived', set waived_by and waived_reason)
    public boolean waiveFine(int fineId, int waivedBy, String waivedReason) throws SQLException {
        String query = "UPDATE fines SET payment_status = 'Waived', waived_by = ?, waived_reason = ? WHERE fine_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, waivedBy);
            stmt.setString(2, waivedReason);
            stmt.setInt(3, fineId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}