package com.Nexus_Library.controllers;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.dao.LibraryItemDAO;
import com.Nexus_Library.dao.TransactionDAO;
import com.Nexus_Library.dao.UserDAO;
import com.Nexus_Library.model.LibraryItem;
import com.Nexus_Library.model.Transaction;
import com.Nexus_Library.model.User;
import com.Nexus_Library.utils.ValidationUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
            return transactionDAO.returnBook(loggedInUser, itemId, item);
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
            List<Transaction> transactions = transactionDAO.getAllActiveTransactionsByUser(loggedInUser);
            if (transactions.isEmpty()) {
                System.out.println("❌ No Active Transactions found.");
            } else {
                System.out.println("\n===== Active Transactions =====");
                for (Transaction transaction : transactions) {
                    System.out.println("Item ID: " + transaction.getItemId() +
                            ", Borrowed: " + transaction.getTransactionDate() +
                            ", Due: " + transaction.getDueDate() +
                            ", Returned: " + (transaction.getReturnDate() != null ? transaction.getReturnDate() : "Not Returned Yet"));}
            }        } catch (Exception e) {
            System.out.println("❌ Error retrieving borrowings: " + e.getMessage());
        }
    }

    public void viewBorrowingHistory(User loggedInUser) {
        if (loggedInUser == null || !ValidationUtils.isValidRole(loggedInUser)) {
            System.out.println("❌ Please login as Student, Faculty, or Researcher to view history.");
            return;
        }

        try {
            // Get all transactions for the user
            List<Transaction> transactions = transactionDAO.getAllTransactionsByUser(loggedInUser.getUserId());

            if (transactions.isEmpty()) {
                System.out.println("❌ No borrowing history found.");
            } else {
                System.out.println("\n===== Borrowing History =====");
                for (Transaction transaction : transactions) {
                    System.out.println("Item ID: " + transaction.getItemId() +
                            ", Borrowed: " + transaction.getTransactionDate() +
                            ", Due: " + transaction.getDueDate() +
                            ", Returned: " + (transaction.getReturnDate() != null ? transaction.getReturnDate() : "Not Returned Yet"));}
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving history: " + e.getMessage());
        }
    }

    public void viewAllActiveTransactions(User loggedInUser) {
        if (loggedInUser == null || !"Admin".equalsIgnoreCase(loggedInUser.getRole())) {
            System.out.println("❌ Only Admin can view active transactions.");
            return;
        }

        try {
            List<Transaction> activeTransactions = transactionDAO.getActiveTransactions();

            if (activeTransactions.isEmpty()) {
                System.out.println("❌ No active transactions found.");
            } else {
                System.out.println("\n===== Active Transactions =====");
                for (Transaction transaction : activeTransactions) {
                    System.out.println("Transaction ID: " + transaction.getTransactionId() +
                            ", User ID: " + transaction.getUserId() +
                            ", Item ID: " + transaction.getItemId() +
                            ", Type: " + transaction.getTransactionType() +
                            ", Borrowed: " + transaction.getTransactionDate() +
                            ", Due: " + transaction.getDueDate() +
                            ", Status: " + transaction.getStatus());
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving active transactions: " + e.getMessage());
        }
    }

    // View all transactions for Admin user
    public void viewAllTransactions(User loggedInUser) {
        if (loggedInUser == null || !"Admin".equalsIgnoreCase(loggedInUser.getRole())) {
            System.out.println("❌ Only Admin can view all transactions.");
            return;
        }

        try {
            List<Transaction> allTransactions = transactionDAO.getAllTransactions();

            if (allTransactions.isEmpty()) {
                System.out.println("❌ No transactions found.");
            } else {
                System.out.println("\n===== All Transactions =====");
                for (Transaction transaction : allTransactions) {
                    System.out.println("Transaction ID: " + transaction.getTransactionId() +
                            ", User ID: " + transaction.getUserId() +
                            ", Item ID: " + transaction.getItemId() +
                            ", Type: " + transaction.getTransactionType() +
                            ", Borrowed: " + transaction.getTransactionDate() +
                            ", Due: " + transaction.getDueDate() +
                            ", Returned: " + (transaction.getReturnDate() != null ? transaction.getReturnDate() : "Not Returned Yet") +
                            ", Status: " + transaction.getStatus());
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving all transactions: " + e.getMessage());
        }
    }


}
