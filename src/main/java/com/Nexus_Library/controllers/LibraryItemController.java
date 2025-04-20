package com.Nexus_Library.controllers;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.dao.LibraryItemDAO;
import com.Nexus_Library.model.LibraryItem;
import com.Nexus_Library.model.User;
import com.Nexus_Library.pattern.creational.LibraryItemFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.Arrays;
import java.util.regex.Pattern;

public class LibraryItemController {
    private final LibraryItemDAO itemDAO;
    private final Scanner scanner;
    private User loggedInUser;

    public LibraryItemController() {
        this.itemDAO = new LibraryItemDAO();
        this.scanner = new Scanner(System.in);
        this.loggedInUser = null;
    }

    public void changeLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public boolean addItem() {
        if (loggedInUser == null || !"Admin".equals(loggedInUser.getRole())) {
            System.out.println("❌ Only Admin can add a library item.");
            return false;
        }

        System.out.println("\n=== Add New Library Item ===");

        System.out.print("Item Type (ebook, physicalbook, researchpaper, audiobook): ");
        String type = scanner.nextLine().trim().toLowerCase();
        String[] validTypes = {"ebook", "physicalbook", "researchpaper", "audiobook"};
        if (!Arrays.asList(validTypes).contains(type)) {
            System.out.println("❌ Invalid item type. Please choose from: " + String.join(", ", validTypes));
            return false;
        }

        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("❌ Title cannot be empty.");
            return false;
        }

        System.out.print("Author: ");
        String author = scanner.nextLine().trim();
        if (!isValidName(author)) {
            System.out.println("❌ Author must contain only letters and spaces, and cannot be empty.");
            return false;
        }

        System.out.print("ISBN (13 digits): ");
        String isbn = scanner.nextLine().trim();
        if (!isValidIsbn(isbn)) {
            System.out.println("❌ ISBN must be exactly 13 digits.");
            return false;
        }

        System.out.print("Available (true/false): ");
        boolean isAvailable;
        try {
            isAvailable = Boolean.parseBoolean(scanner.nextLine().trim());
        } catch (Exception e) {
            System.out.println("❌ Availability must be 'true' or 'false'.");
            return false;
        }

        System.out.print("Extra Parameter (e.g., file format for ebook, copies for physicalbook, etc.): ");
        String extraParam = scanner.nextLine().trim();
        if (extraParam.isEmpty()) {
            System.out.println("❌ Extra parameter cannot be empty.");
            return false;
        }
        int extraParamInt = 0;
        if ("physicalbook".equals(type) || "audiobook".equals(type)) {
            try {
                extraParamInt = Integer.parseInt(extraParam);
                if (extraParamInt <= 0) {
                    System.out.println("❌ Extra parameter must be a positive number for physicalbook (copies) or audiobook (duration).");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Extra parameter must be a number for physicalbook (copies) or audiobook (duration).");
                return false;
            }
        }

        Timestamp addedDate = new Timestamp(System.currentTimeMillis());
        LibraryItem item = LibraryItemFactory.createItem(type, 0, title, author, isbn, isAvailable, addedDate, extraParam);
        try {
            boolean success = itemDAO.addItem(item);
            if (success) {
                System.out.println("✅ Item added successfully! Item ID: " + item.getItemId());
            } else {
                System.out.println("❌ Failed to add item.");
            }
            return success;
        } catch (Exception e) {
            System.out.println("❌ Error adding item: " + e.getMessage());
            return false;
        }
    }

    public boolean updateAvailability(int itemId, boolean isAvailable) {
        if (loggedInUser == null || !"Admin".equals(loggedInUser.getRole())) {
            System.out.println("❌ Only Admin can update availability.");
            return false;
        }
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

    // Validation methods
    public boolean isValidName(String name) {
        return name != null && !name.isEmpty() && name.matches("[a-zA-Z\\s]+");
    }

    public boolean isValidIsbn(String isbn) {
        return isbn != null && isbn.matches("\\d{13}");
    }

    public void close() {
        scanner.close();
    }
}