package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.*;
import com.Nexus_Library.pattern.creational.LibraryItemFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryItemDAO {
    public boolean addItem(LibraryItem item) throws SQLException {
        String query = "INSERT INTO library_items (item_id, title, author, isbn, is_available, added_date, type, extra_param) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, item.getItemId());
            stmt.setString(2, item.getTitle());
            stmt.setString(3, item.getAuthor());
            stmt.setString(4, item.getIsbn());
            stmt.setBoolean(5, item.isAvailable());
            stmt.setTimestamp(6, item.getAddedDate());
            stmt.setString(7, item.getItemType());
            stmt.setString(8, getExtraParam(item)); // Handles type-specific data
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0 && item.getItemId() == 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    item.setItemId(generatedKeys.getInt(1));
                }
            }
            return rowsAffected > 0;
        }
    }

    public boolean updateAvailability(int itemId, boolean isAvailable) throws SQLException {

        try {
            String query = "UPDATE library_items SET is_available = ? WHERE item_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setBoolean(1, isAvailable);
                stmt.setInt(2, itemId);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (Exception e) {
            System.out.println("❌ Error updating availability: " + e.getMessage());
            return false;
        }
    }


    public List<LibraryItem> searchLibraryItems(String searchQuery) throws SQLException {
        List<LibraryItem> itemList = new ArrayList<>();

        String query = "SELECT * FROM library_items WHERE " +
                "title ILIKE ? OR " +
                "author ILIKE ? OR " +
                "isbn ILIKE ? OR " +
                "type ILIKE ? OR " +
                "extra_param ILIKE ? OR " +
                "CAST(added_date AS TEXT) ILIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String likeQuery = "%" + searchQuery + "%";

            for (int i = 1; i <= 6; i++) {
                stmt.setString(i, likeQuery);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itemList.add(LibraryItemFactory.createItem(rs.getString("type"), rs.getInt("item_id"),
                                    rs.getString("title"),
                                    rs.getString("author"),
                                    rs.getString("isbn"),
                                    rs.getBoolean("is_available"),
                                    rs.getTimestamp("added_date"),
                                    rs.getString("extra_param")
                            )
                    );
                }
            }
        } catch (SQLException e) {
            throw e;
        }

        return itemList;
    }

    public boolean borrowBook(User loggedInUser, int itemId, LibraryItem item) throws SQLException {
        try {

            String query = "INSERT INTO transactions (user_id, item_id, transaction_type, due_date, status) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, loggedInUser.getUserId());
                stmt.setInt(2, itemId);
                stmt.setString(3, "Borrow");
                stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis() + (item.getMaxBorrowDays() * 24 * 60 * 60 * 1000L)));
                stmt.setString(5, "Active");
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0 && this.updateAvailability(itemId, false)) {
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
            String query = "UPDATE transactions SET return_date = ?, status = ? WHERE user_id = ? AND item_id = ? AND status = 'Active'";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                stmt.setString(2, "Completed");
                stmt.setInt(3, loggedInUser.getUserId());
                stmt.setInt(4, itemId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0 && this.updateAvailability(itemId, true)) {
                    System.out.println("✅ Book returned successfully!");

                    checkAndApplyFine(loggedInUser, itemId, item); // Check for overdue and apply fine if needed
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

    public LibraryItem getItemById(int itemId) {
        try {
            String query = "SELECT * FROM library_items WHERE item_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, itemId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String type = rs.getString("type");
                    return LibraryItemFactory.createItem(
                            type.toLowerCase(),
                            rs.getInt("item_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getBoolean("is_available"),
                            rs.getTimestamp("added_date"),
                            rs.getString("extra_param")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error retrieving item: " + e.getMessage());
        }
        return null;
    }

    public boolean viewBorrowing(User loggedInUser) throws SQLException {
        try {
            String query = "SELECT item_id FROM transactions WHERE user_id = ? AND status = 'Active'";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, loggedInUser.getUserId());
                ResultSet rs = stmt.executeQuery();
                List<Integer> borrowedItems = new ArrayList<>();
                while (rs.next()) {
                    borrowedItems.add(rs.getInt("item_id"));
                }
                System.out.println("Current Borrowings (" + borrowedItems.size() + "): " + borrowedItems);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return false;
    }

    public boolean updateLibraryItem(LibraryItem item) throws SQLException {
        String query = "UPDATE library_items SET title = ?, author = ?, isbn = ?, is_available = ?, extra_param = ? WHERE item_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the parameters for the update
            stmt.setString(1, item.getTitle());
            stmt.setString(2, item.getAuthor());
            stmt.setString(3, item.getIsbn());
            stmt.setBoolean(4, item.isAvailable());
            stmt.setString(5, getExtraParam(item));
            stmt.setInt(6, item.getItemId());

            // Execute the update
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private String getExtraParam(LibraryItem item) {
        if (item instanceof EBook) return ((EBook) item).getFileFormat();
        if (item instanceof PhysicalBook) return String.valueOf(((PhysicalBook) item).getCopies());
        if (item instanceof ResearchPaper) return ((ResearchPaper) item).getPublication();
        if (item instanceof AudioBook) return String.valueOf(((AudioBook) item).getDurationMinutes());
        return null;
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


    private void checkAndApplyFine(User loggedInUser, int itemId, LibraryItem item) {
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
                        fineDAO.createFine(fine);
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
}