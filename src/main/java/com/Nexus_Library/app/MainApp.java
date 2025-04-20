package com.Nexus_Library.app;

import com.Nexus_Library.controllers.LibraryController;
import com.Nexus_Library.controllers.LibraryItemController;
import com.Nexus_Library.controllers.UserController;
import com.Nexus_Library.model.User;

import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        UserController userController = new UserController();
        LibraryItemController libraryItemController = new LibraryItemController();
        LibraryController libraryController = new LibraryController();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            User loggedInUser = userController.getLoggedInUser();

            if (loggedInUser == null) {
                // Show login/registration menu if not logged in
                System.out.println("\n===== Welcome to Nexus Library =====");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice (1-3): ");

                int choice;
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                } catch (Exception e) {
                    System.out.println("❌ Invalid input. Please enter a number (1-3).");
                    scanner.nextLine(); // Clear invalid input
                    continue;
                }

                switch (choice) {
                    case 1:
                        userController.register();
                        break;
                    case 2:
                        User newUser = userController.login();
                        if (newUser != null) {
                            libraryItemController.changeLoggedInUser(newUser);
                            libraryController.setLoggedInUser(newUser);
                            System.out.println("✅ You are now logged in as " + newUser.getFirstName() + " (" + newUser.getRole() + ").");
                        }
                        break;
                    case 3:
                        System.out.println("👋 Exiting... Thank you for using Nexus Library!");
                        userController.close();
                        libraryItemController.close();
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("⚠️ Invalid choice. Please enter a number between 1 and 3.");
                }
            } else {
                // Show operational menu if logged in
                System.out.println("\n===== Nexus Library - Welcome, " + loggedInUser.getFirstName() + " (" + loggedInUser.getRole() + ") =====");
                System.out.println("1. Borrow Book");
                System.out.println("2. Return Book");
                System.out.println("3. View Current Borrowings");
                System.out.println("4. View Borrowing History");
                System.out.println("5. Pay Fine");
                System.out.println("6. Update Profile");
                if ("Admin".equals(loggedInUser.getRole())) {
                    System.out.println("7. Add Library Item");
                    System.out.println("8. Delete Book");
                    System.out.println("9. Update Book Info");
                    System.out.println("10. Take Fine");
                    System.out.println("11. Logout");
                } else {
                    System.out.println("7. Logout");
                }
                System.out.print("Enter your choice (1-" + (loggedInUser.getRole().equals("Admin") ? "11" : "7") + "): ");

                int choice;
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                } catch (Exception e) {
                    System.out.println("❌ Invalid input. Please enter a number (1-" + (loggedInUser.getRole().equals("Admin") ? "11" : "7") + ").");
                    scanner.nextLine(); // Clear invalid input
                    continue;
                }

                switch (choice) {
                    case 1:
                        libraryController.borrowBook();
                        break;
                    case 2:
                        libraryController.returnBook();
                        break;
                    case 3:
                        libraryController.viewCurrentBorrowings();
                        break;
                    case 4:
                        libraryController.viewBorrowingHistory();
                        break;
                    case 5:
                        libraryController.payFine();
                        break;
                    case 6:
                        libraryController.updateProfile();
                        break;
                    case 7:
                        if ("Admin".equals(loggedInUser.getRole())) {
                            libraryItemController.addItem();
                        } else {
                            userController.getLoggedInUser().setUserId(0); // Reset loggedInUser indirectly
                            loggedInUser = null; // Log out
                            System.out.println("✅ Logged out successfully!");
                        }
                        break;
                    case 8:
                        if ("Admin".equals(loggedInUser.getRole())) {
                            libraryController.deleteBook();
                        } else {
                            System.out.println("⚠️ Invalid choice for your role.");
                        }
                        break;
                    case 9:
                        if ("Admin".equals(loggedInUser.getRole())) {
                            libraryController.updateBookInfo();
                        } else {
                            System.out.println("⚠️ Invalid choice for your role.");
                        }
                        break;
                    case 10:
                        if ("Admin".equals(loggedInUser.getRole())) {
                            libraryController.takeFine();
                        } else {
                            System.out.println("⚠️ Invalid choice for your role.");
                        }
                        break;
                    case 11:
                        if ("Admin".equals(loggedInUser.getRole())) {
                            userController.getLoggedInUser().setUserId(0); // Reset loggedInUser indirectly
                            loggedInUser = null; // Log out
                            System.out.println("✅ Logged out successfully!");
                        } else {
                            System.out.println("⚠️ Invalid choice for your role.");
                        }
                        break;
                    default:
                        System.out.println("⚠️ Invalid choice. Please enter a number between 1 and " + (loggedInUser.getRole().equals("Admin") ? "11" : "7") + ".");
                }
            }
        }
    }
}