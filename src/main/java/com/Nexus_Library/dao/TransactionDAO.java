package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.Transaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("user_id"),
                        rs.getInt("loan_id"),
                        rs.getInt("fine_id"),
                        rs.getString("transaction_type"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("transaction_date"),
                        rs.getString("status")
                ));
            }
        }
        return transactions;
    }
}
//package com.Nexus_Library.dao;
//
//import com.Nexus_Library.config.DBConnection;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class TransactionDAO {
//    public List<String> getUserTransactions(int userId) throws SQLException {
//        List<String> transactions = new ArrayList<>();
//        String query = "SELECT transaction_type, amount, transaction_date FROM transactions WHERE user_id = ?";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setInt(1, userId);
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    transactions.add(rs.getString("transaction_type") + ": $" + rs.getDouble("amount") + " on " + rs.getTimestamp("transaction_date"));
//                }
//            }
//        }
//        return transactions;
//    }
//
//    public void addTransaction(int userId, int loanId, String type, double amount) throws SQLException {
//        String query = "INSERT INTO transactions (user_id, loan_id, transaction_type, amount) VALUES (?, ?, ?, ?)";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setInt(1, userId);
//            stmt.setInt(2, loanId);
//            stmt.setString(3, type);
//            stmt.setDouble(4, amount);
//            stmt.executeUpdate();
//        }
//    }
//}