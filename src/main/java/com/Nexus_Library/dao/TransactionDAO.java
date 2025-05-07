package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // Create a new transaction record
    public boolean createTransaction(Transaction transaction) throws SQLException {
        String query = "INSERT INTO transactions (user_id, item_id, transaction_type, transaction_date, due_date, status) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING transaction_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transaction.getUserId());
            stmt.setInt(2, transaction.getItemId());
            stmt.setString(3, transaction.getTransactionType());
            stmt.setTimestamp(4, transaction.getTransactionDate());
            stmt.setTimestamp(5, transaction.getDueDate());
            stmt.setString(6, transaction.getStatus());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                transaction.setTransactionId(rs.getInt("transaction_id"));
                return true;
            }
            return false;
        }
    }

    // Retrieve a transaction by transaction_id
    public Transaction getTransactionById(int transactionId) throws SQLException {
        String query = "SELECT * FROM transactions WHERE transaction_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("user_id"),
                        rs.getInt("item_id"),
                        rs.getString("transaction_type"),
                        rs.getTimestamp("transaction_date"),
                        rs.getTimestamp("due_date"),
                        rs.getTimestamp("return_date"),
                        rs.getString("status")
                );
            }
            return null;
        }
    }

    // Retrieve all transactions for a user
    public List<Transaction> getTransactionsByUser(int userId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("user_id"),
                        rs.getInt("item_id"),
                        rs.getString("transaction_type"),
                        rs.getTimestamp("transaction_date"),
                        rs.getTimestamp("due_date"),
                        rs.getTimestamp("return_date"),
                        rs.getString("status")
                );
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    // Retrieve active transaction for a user and item
    public Transaction getActiveTransaction(int userId, int itemId) throws SQLException {
        String query = "SELECT * FROM transactions WHERE user_id = ? AND item_id = ? AND status = 'Active'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("user_id"),
                        rs.getInt("item_id"),
                        rs.getString("transaction_type"),
                        rs.getTimestamp("transaction_date"),
                        rs.getTimestamp("due_date"),
                        rs.getTimestamp("return_date"),
                        rs.getString("status")
                );
            }
            return null;
        }
    }

    // Update a transaction (e.g., set return_date, status)
    public boolean updateTransaction(Transaction transaction) throws SQLException {
        String query = "UPDATE transactions SET transaction_type = ?, due_date = ?, return_date = ?, status = ? " +
                "WHERE transaction_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transaction.getTransactionType());
            stmt.setTimestamp(2, transaction.getDueDate());
            stmt.setTimestamp(3, transaction.getReturnDate());
            stmt.setString(4, transaction.getStatus());
            stmt.setInt(5, transaction.getTransactionId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}