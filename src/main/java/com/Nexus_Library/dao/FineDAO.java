package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.Fine;
import com.Nexus_Library.model.LibraryItem;
import com.Nexus_Library.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FineDAO {

    // Create a new fine record
    private boolean createFine(Fine fine) throws SQLException {
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

//    // Update fine payment status (e.g., mark as Paid)
//    public boolean updateFinePayment(int fineId, String paymentStatus, Timestamp paymentDate) throws SQLException {
//        String query = "UPDATE fines SET payment_status = ?, payment_date = ? WHERE fine_id = ?";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setString(1, paymentStatus);
//            stmt.setTimestamp(2, paymentDate);
//            stmt.setInt(3, fineId);
//
//            int rowsAffected = stmt.executeUpdate();
//            return rowsAffected > 0;
//        }
//    }

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





    private double calculateFine(Timestamp dueDate, User loggedInUser) {
        FineSettingDAO settingsDAO = new FineSettingDAO();
        try {
            int fineRateRupees = settingsDAO.getFinePerDay(loggedInUser.getRole()); // Fetch fine rate in rupees
            long daysOverdue = (System.currentTimeMillis() - dueDate.getTime()) / (1000 * 60 * 60 * 24);
            return daysOverdue * fineRateRupees; // Fine amount in rupees
        } catch (SQLException e) {
            return 0.0;
        }
    }


    public void checkAndApplyFine(User loggedInUser, int itemId, LibraryItem item) {
        try {
            String query = "SELECT transaction_id, due_date FROM transactions WHERE user_id = ? AND item_id = ? AND status = 'Active'";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, loggedInUser.getUserId());
                stmt.setInt(2, itemId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int transactionId = rs.getInt("transaction_id");
                    Timestamp dueDate = rs.getTimestamp("due_date");
                    if (System.currentTimeMillis() > dueDate.getTime()) {
                        double fineAmount = calculateFine(dueDate,loggedInUser);
                        // Create a new Fine object
                        Fine fine = new Fine(
                                0, // fine_id will be set by the database
                                transactionId,
                                loggedInUser.getUserId(),
                                fineAmount,
                                new Timestamp(System.currentTimeMillis()),
                                "Pending",
                                null, // payment_date
                                null, // waived_by
                                null  // waived_reason
                        );
                        // Use FineDAO to insert the fine
                        FineDAO fineDAO = new FineDAO();
                        if(!fineDAO.createFine(fine)){
                            throw new SQLException();
                        }

                        System.out.println("⚠️ Overdue! Fine of ₹" + fineAmount + " applied.");
                        // Update transaction status to Overdue
                        String updateQuery = "UPDATE transactions SET status = 'Overdue' WHERE transaction_id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, transactionId);
                            updateStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error checking fine: " + e.getMessage());
        }
    }




    public int getOutstandingFineAmount(int userId) throws SQLException {
        String query = "SELECT SUM(fine_amount) AS total_fine FROM transactions WHERE user_id = ? AND fine_amount > 0 AND payment_status = 'Pending'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_fine");
                }
            }
        }

        return 0;
    }




    public boolean payAllPendingFines(int userId) throws SQLException {
        String query = "UPDATE fines SET payment_status = 'Paid', payment_date = CURRENT_TIMESTAMP " +
                "WHERE user_id = ? AND payment_status = 'Pending'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }

    public List<String> getPendingFinesDetails(int userId) throws SQLException {
        String query = "SELECT fine_id, transaction_id, fine_amount FROM fines WHERE user_id = ? AND payment_status = 'Pending'";
        List<String> details = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    details.add("Fine ID: " + rs.getInt("fine_id") +
                            ", Transaction ID: " + rs.getInt("transaction_id") +
                            ", Amount: ₹" + rs.getBigDecimal("fine_amount"));
                }
            }
        }

        return details;
    }

}