package com.Nexus_Library.controllers;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.dao.LibraryItemDAO;
import com.Nexus_Library.dao.TransactionDAO;
import com.Nexus_Library.dao.UserDAO;
import com.Nexus_Library.model.LibraryItem;
import com.Nexus_Library.model.User;
import com.Nexus_Library.utils.ValidationUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TransactionController {
    private final Scanner scanner;
    private LibraryItemDAO libraryItemDAO;
    private TransactionDAO transactionDAO ;
    public TransactionController() {
        this.scanner = new Scanner(System.in);
        this.libraryItemDAO = new LibraryItemDAO();
        this.transactionDAO = new TransactionDAO();
    }


    // Student/Faculty/Researcher Operations
    public boolean borrowBook(User loggedInUser) {
        if (loggedInUser == null || !ValidationUtils.isValidRole(loggedInUser)) {
            System.out.println("❌ Please login as Student, Faculty, or Researcher to borrow a book.");
            return false;
        }

        System.out.print("Enter Item ID to borrow: ");
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
        if (!item.isAvailable()) {
            System.out.println("❌ Item is not available.");
            return false;
        }


        try {
            return transactionDAO.borrowBook(loggedInUser, itemId, item);
        } catch (SQLException e) {
            System.out.println("❌ Error borrowing book: " + e.getMessage());
        }
        return false;

    }

    public boolean returnBook(User loggedInUser) {
        if (loggedInUser == null || !ValidationUtils.isValidRole(loggedInUser)) {
            System.out.println("❌ Please login as Student, Faculty, or Researcher to return a book.");
            return false;
        }

        System.out.print("Enter Item ID to return: ");
        int itemId;
        try {
            itemId = Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            System.out.println("❌ Invalid Item ID.");
            return false;
        }

        LibraryItem item = libraryItemDAO.getItemById(itemId);;

        try {
            return transactionDAO.borrowBook(loggedInUser, itemId, item);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void viewCurrentBorrowings(User loggedInUser) {
        if (loggedInUser == null || !ValidationUtils.isValidRole(loggedInUser)) {
            System.out.println("❌ Please login as Student, Faculty, or Researcher to view borrowings.");
            return;
        }

        try {

        } catch (Exception e) {
            System.out.println("❌ Error retrieving borrowings: " + e.getMessage());
        }
    }

    public void viewBorrowingHistory(User loggedInUser) {
        if (loggedInUser == null || !ValidationUtils.isValidRole(loggedInUser)) {
            System.out.println("❌ Please login as Student, Faculty, or Researcher to view history.");
            return;
        }

        try {
            String query = "SELECT item_id, transaction_date, due_date, return_date, fine_amount FROM transactions WHERE user_id = ? AND status IN ('Completed', 'Overdue')";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, loggedInUser.getUserId());
                ResultSet rs = stmt.executeQuery();
                System.out.println("Borrowing History:");
                while (rs.next()) {
                    System.out.println("Item ID: " + rs.getInt("item_id") +
                            ", Borrowed: " + rs.getTimestamp("transaction_date") +
                            ", Due: " + rs.getTimestamp("due_date") +
                            ", Returned: " + rs.getTimestamp("return_date") +
                            ", Fine: $" + rs.getDouble("fine_amount"));
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error retrieving history: " + e.getMessage());
        }
    }



}
