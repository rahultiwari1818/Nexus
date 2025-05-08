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


    // search methods -------------------------------------------------------------------------

    public List<LibraryItem> searchByISBN(String isbn) throws SQLException {
        List<LibraryItem> itemList = new ArrayList<>();
        String query = "SELECT * FROM library_items WHERE isbn ILIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + isbn + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itemList.add(LibraryItemFactory.createItem(
                            rs.getString("type"),
                            rs.getInt("item_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getBoolean("is_available"),
                            rs.getTimestamp("added_date"),
                            rs.getString("extra_param")
                    ));
                }
            }
        }

        return itemList;
    }


    public List<LibraryItem> searchByAuthor(String author) throws SQLException {
        List<LibraryItem> itemList = new ArrayList<>();
        String query = "SELECT * FROM library_items WHERE author ILIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + author + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itemList.add(LibraryItemFactory.createItem(
                            rs.getString("type"),
                            rs.getInt("item_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getBoolean("is_available"),
                            rs.getTimestamp("added_date"),
                            rs.getString("extra_param")
                    ));
                }
            }
        }

        return itemList;
    }


    public List<LibraryItem> searchByTitle(String title) throws SQLException {
        List<LibraryItem> itemList = new ArrayList<>();
        String query = "SELECT * FROM library_items WHERE title ILIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + title + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itemList.add(LibraryItemFactory.createItem(
                            rs.getString("type"),
                            rs.getInt("item_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getBoolean("is_available"),
                            rs.getTimestamp("added_date"),
                            rs.getString("extra_param")
                    ));
                }
            }
        }

        return itemList;
    }

//    ----------------------------------------------------------------------------------------------------


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


}