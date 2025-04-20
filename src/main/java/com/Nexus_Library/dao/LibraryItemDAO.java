package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.*;

import java.sql.*;

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

    private String getExtraParam(LibraryItem item) {
        if (item instanceof EBook) return ((EBook) item).getFileFormat();
        if (item instanceof PhysicalBook) return String.valueOf(((PhysicalBook) item).getCopies());
        if (item instanceof ResearchPaper) return ((ResearchPaper) item).getPublication();
        if (item instanceof AudioBook) return String.valueOf(((AudioBook) item).getDurationMinutes());
        return null;
    }
}