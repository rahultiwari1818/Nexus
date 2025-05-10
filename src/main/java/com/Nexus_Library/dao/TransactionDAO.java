package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.BorrowingSetting;
import com.Nexus_Library.model.LibraryItem;
import com.Nexus_Library.model.Transaction;
import com.Nexus_Library.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

     private  final LibraryItemDAO libraryItemDAO;
     private  final FineDAO fineDAO;
     private final BorrowingSettingDAO borrowingSettingDAO;

     public TransactionDAO(){
         libraryItemDAO = new LibraryItemDAO();
         fineDAO = new FineDAO();
         borrowingSettingDAO = new BorrowingSettingDAO();
     }

    public boolean borrowBook(User loggedInUser, int itemId, LibraryItem item) throws SQLException {
        try {

            int activeTransactions = getAllActiveTransactionsCountByUser(loggedInUser);
            int allowedTransactions = borrowingSettingDAO.getMaxBorrowPerUserType(loggedInUser);

            if(activeTransactions >= allowedTransactions) {
                System.out.println("❌ Max Borrowing Limit Reached .!");
                return false;
            }

                

            int getMaxBorrowedDays = borrowingSettingDAO.getMaxBorrowPerUserType(loggedInUser);

            String query = "INSERT INTO transactions (user_id, item_id, transaction_type, due_date, status) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, loggedInUser.getUserId());
                stmt.setInt(2, itemId);
                stmt.setString(3, "Borrow");
                stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis() + (getMaxBorrowedDays * 24 * 60 * 60 * 1000L)));
                stmt.setString(5, "Active");
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0 && libraryItemDAO.updateAvailability(itemId, false)) {
                    System.out.println("✅ Book borrowed successfully! Due date: " + new Timestamp(System.currentTimeMillis() + (item.getMaxBorrowDays() * 24 * 60 * 60 * 1000L)));
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error borrowing book: " + e.getMessage());
        }
        return false;
    }

    public boolean returnBook(User loggedInUser, int itemId, LibraryItem item) throws SQLException {
        try {
            boolean isFineApplied = fineDAO.checkAndApplyFine(loggedInUser, itemId, item); // Check for overdue and apply fine if needed

            String query = "UPDATE transactions SET return_date = ?, status = ? WHERE user_id = ? AND item_id = ? AND status = '"
                    + (isFineApplied ? "Overdue" : "Active") + "'";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                stmt.setString(2, "Completed");
                stmt.setInt(3, loggedInUser.getUserId());
                stmt.setInt(4, itemId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0 && libraryItemDAO.updateAvailability(itemId, true)) {
                    System.out.println("✅ Book returned successfully!");

                    return true;
                } else {
                    System.out.println("❌ No active borrowing found for this item.");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error returning book: " + e.getMessage());
        }
        return false;
    }



    public int getAllActiveTransactionsCountByUser(User loggedInUser) throws SQLException {
        String query = "SELECT COUNT(*) AS active_count FROM transactions WHERE user_id = ? AND status = 'Active'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, loggedInUser.getUserId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("active_count");
            }
        }

        return 0; // If query fails or user has no active transactions
    }

    public List<Transaction> getAllActiveTransactionsByUser(User loggedInUser) throws SQLException{
        String query = "SELECT * FROM transactions WHERE user_id = ? AND status = 'Active'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, loggedInUser.getUserId());
            ResultSet rs = stmt.executeQuery();
            List<Transaction> transactions = new ArrayList<>();
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
            return transactions;
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
    public List<Transaction> getAllTransactionsByUser(int userId) throws SQLException {
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
    public List<Transaction> getActiveTransactions() throws SQLException {
        String query = "SELECT * FROM transactions WHERE  status = 'Active'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            List<Transaction> transactions = new ArrayList<>();

            while (rs.next()) {
                transactions.add( new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("user_id"),
                        rs.getInt("item_id"),
                        rs.getString("transaction_type"),
                        rs.getTimestamp("transaction_date"),
                        rs.getTimestamp("due_date"),
                        rs.getTimestamp("return_date"),
                        rs.getString("status")
                ));
            }
            return transactions;
        }
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        String query = "SELECT * FROM transactions";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            List<Transaction> transactions = new ArrayList<>();

            while (rs.next()) {
                transactions.add( new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("user_id"),
                        rs.getInt("item_id"),
                        rs.getString("transaction_type"),
                        rs.getTimestamp("transaction_date"),
                        rs.getTimestamp("due_date"),
                        rs.getTimestamp("return_date"),
                        rs.getString("status")
                ));
            }
            return transactions;
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