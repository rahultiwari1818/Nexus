package com.Nexus_Library.controllers;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.dao.LibraryItemDAO;
import com.Nexus_Library.dao.UserDAO;
import com.Nexus_Library.model.*;
import com.Nexus_Library.utils.ValidationUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class LibraryController {
    private final UserDAO userDAO;
    private final LibraryItemController itemController;
    private final Scanner scanner;
    private LibraryItemDAO libraryItemDAO;

    public LibraryController() {
        this.userDAO = new UserDAO();
        this.itemController = new LibraryItemController();
        this.scanner = new Scanner(System.in);
        this.libraryItemDAO = new LibraryItemDAO();
    }




    public boolean updateProfile(User loggedInUser) {
        if (loggedInUser == null || !ValidationUtils.isValidRole( loggedInUser)) {
            System.out.println("❌ Please login as Student, Faculty, or Researcher to update profile.");
            return false;
        }

        System.out.print("New First Name: ");
        String firstName = scanner.nextLine().trim();
        if (!ValidationUtils.isValidName(firstName)) { // Use LibraryItemController's validation
            System.out.println("❌ First Name must contain only letters and spaces, and cannot be empty.");
            return false;
        }

        System.out.print("New Last Name: ");
        String lastName = scanner.nextLine().trim();
        if (!ValidationUtils.isValidName(lastName)) {
            System.out.println("❌ Last Name must contain only letters and spaces, and cannot be empty.");
            return false;
        }

        System.out.print("New Email: ");
        String email = scanner.nextLine().trim();
        if (!isValidEmail(email)) {
            System.out.println("❌ Invalid email format (e.g., must contain @ and .).");
            return false;
        }
        if (isEmailExists(email) && !email.equals(loggedInUser.getEmail())) {
            System.out.println("❌ Email already registered.");
            return false;
        }

        System.out.print("New Password: ");
        String password = scanner.nextLine().trim();
        if (!isValidPassword(password)) {
            System.out.println("❌ Password must be at least 6 characters long and include at least one letter and one number.");
            return false;
        }

        try {
            String query = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ? WHERE user_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setString(3, email);
                stmt.setString(4, password);
                stmt.setInt(5, loggedInUser.getUserId());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    loggedInUser.setFirstName(firstName);
                    loggedInUser.setLastName(lastName);
                    loggedInUser.setEmail(email);
                    loggedInUser.setPassword(password);
                    System.out.println("✅ Profile updated successfully!");
                    return true;
                } else {
                    System.out.println("❌ Update failed.");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error updating profile: " + e.getMessage());
        }
        return false;
    }

    // Admin Operations
    public boolean deleteBook(User loggedInUser) {
        if (loggedInUser == null || !"Admin".equals(loggedInUser.getRole())) {
            System.out.println("❌ Only Admin can delete a book.");
            return false;
        }

        System.out.print("Enter Item ID to delete: ");
        int itemId;
        try {
            itemId = Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            System.out.println("❌ Invalid Item ID.");
            return false;
        }

        try {
            String query = "DELETE FROM library_items WHERE item_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, itemId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("✅ Book deleted successfully!");
                    return true;
                } else {
                    System.out.println("❌ No book found with ID " + itemId);
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error deleting book: " + e.getMessage());
        }
        return false;
    }

    public boolean updateBookInfo(User loggedInUser) {
        if (loggedInUser == null || !"Admin".equals(loggedInUser.getRole())) {
            System.out.println("❌ Only Admin can update book info.");
            return false;
        }

        System.out.print("Enter Item ID to update: ");
        int itemId;
        try {
            itemId = Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            System.out.println("❌ Invalid Item ID.");
            return false;
        }

        LibraryItem item = libraryItemDAO.getItemById(itemId);
        if (item == null) {
            System.out.println("❌ Item not found.");
            return false;
        }

        System.out.print("New Title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) title = item.getTitle();

        System.out.print("New Author: ");
        String author = scanner.nextLine().trim();
        if (!ValidationUtils.isValidName(author) && !author.isEmpty()) {
            System.out.println("❌ Author must contain only letters and spaces.");
            return false;
        }
        if (author.isEmpty()) author = item.getAuthor();

        System.out.print("New ISBN (13 digits, leave empty to keep current): ");
        String isbn = scanner.nextLine().trim();
        if (!isbn.isEmpty() && !ValidationUtils.isValidIsbn(isbn)) {
            System.out.println("❌ ISBN must be exactly 13 digits.");
            return false;
        }
        if (isbn.isEmpty()) isbn = item.getIsbn();

        System.out.print("New Availability (true/false, leave empty to keep current): ");
        boolean isAvailable;
        String availabilityInput = scanner.nextLine().trim();
        if (availabilityInput.isEmpty()) {
            isAvailable = item.isAvailable();
        } else {
            try {
                isAvailable = Boolean.parseBoolean(availabilityInput);
            } catch (Exception e) {
                System.out.println("❌ Availability must be 'true' or 'false'.");
                return false;
            }
        }

        System.out.print("New Extra Parameter (leave empty to keep current): ");
        String extraParam = scanner.nextLine().trim();
        if (extraParam.isEmpty()) {
            extraParam = getCurrentExtraParam(item);
        } else {
            extraParam = getUpdatedExtraParam(item, extraParam);
            if (extraParam == null) return false;
        }

        try {
            String query = "UPDATE library_items SET title = ?, author = ?, isbn = ?, is_available = ?, extra_param = ? WHERE item_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, title);
                stmt.setString(2, author);
                stmt.setString(3, isbn);
                stmt.setBoolean(4, isAvailable);
                stmt.setString(5, extraParam);
                stmt.setInt(6, itemId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("✅ Book info updated successfully!");
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error updating book: " + e.getMessage());
        }
        return false;
    }


    // ----- changed

    public boolean takeFine(User loggedInUser) {
        if (loggedInUser == null || !"Admin".equals(loggedInUser.getRole())) {
            System.out.println("❌ Only Admin can take a fine.");
            return false;
        }

        System.out.print("Enter User ID to check fines: ");
        int userId;
        try {
            userId = Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            System.out.println("❌ Invalid User ID.");
            return false;
        }

        try {
            String query = "SELECT transaction_id, fine_amount FROM transactions WHERE user_id = ? AND status = 'Overdue' AND paid_date IS NULL";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int transactionId = rs.getInt("transaction_id");
                    double fineAmount = rs.getDouble("fine_amount");
                    System.out.println("Outstanding Fine: $" + fineAmount + " for Transaction ID: " + transactionId);
                    System.out.print("Confirm fine collection (yes/no): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();
                    if ("yes".equals(confirm)) {
                        String updateQuery = "UPDATE transactions SET paid_date = ?, status = 'Completed' WHERE transaction_id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                            updateStmt.setInt(2, transactionId);
                            int rowsAffected = updateStmt.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("✅ Fine of $" + fineAmount + " collected successfully!");
                                return true;
                            }
                        }
                    } else {
                        System.out.println("❌ Fine collection cancelled.");
                    }
                } else {
                    System.out.println("✅ No outstanding fines for user ID " + userId);
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error taking fine: " + e.getMessage());
        }
        return false;
    }


    // Helper methods



    //untouched

    private String getCurrentExtraParam(LibraryItem item) {
        if (item instanceof EBook) return ((EBook) item).getFileFormat();
        if (item instanceof PhysicalBook) return String.valueOf(((PhysicalBook) item).getCopies());
        if (item instanceof ResearchPaper) return ((ResearchPaper) item).getPublication();
        if (item instanceof AudioBook) return String.valueOf(((AudioBook) item).getDurationMinutes());
        return null;
    }

    private String getUpdatedExtraParam(LibraryItem item, String extraParam) {
        if ("physicalbook".equals(item.getItemType()) || "audiobook".equals(item.getItemType())) {
            try {
                int param = Integer.parseInt(extraParam);
                if (param <= 0) {
                    System.out.println("❌ Extra parameter must be a positive number.");
                    return null;
                }
                return extraParam;
            } catch (NumberFormatException e) {
                System.out.println("❌ Extra parameter must be a number.");
                return null;
            }
        }
        return extraParam;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) return false;
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        return hasLetter && hasNumber;
    }

    private boolean isEmailExists(String email) {
        try {
            return userDAO.loginUser(email, "") != null;
        } catch (Exception e) {
            System.out.println("❌ Error checking email existence: " + e.getMessage());
            return false;
        }
    }
}