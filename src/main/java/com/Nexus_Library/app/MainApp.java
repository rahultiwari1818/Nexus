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
                System.out.println("1. Register : ");
                System.out.println("2. Login : ");
                System.out.println("3. Search Book : ");
                System.out.println("4. Exit : ");
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

                        break;
                    case 4:
                        System.out.println("👋 Exiting... Thank you for using Nexus Library!");
                        userController.close();
                        libraryItemController.close();
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("⚠️ Invalid choice. Please enter a number between 1 and 3.");
                }
            }
            else if (loggedInUser.getRole().equals("Admin")) {
                // Show Admin menu
                System.out.println("\n=== Admin Menu ===");
                System.out.println("1. Add Library Item");
                System.out.println("2. Delete Book");
                System.out.println("3. Update Book Info");
                System.out.println("4. Take Fine");
                System.out.println("5. Update Profile");
                System.out.println("6. View Users");
                System.out.println("7. Logout");

                System.out.print("Enter your choice (1-6): ");

                int choice;
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                } catch (Exception e) {
                    System.out.println("❌ Invalid input. Please enter a number between 1 and 6.");
                    scanner.nextLine(); // Clear invalid input
                    return; // Exit the method or loop iteration
                }

                switch (choice) {
                    case 1:
                        libraryItemController.addItem();
                        break;
                    case 2:
                        libraryController.deleteBook();
                        break;
                    case 3:
                        libraryController.updateBookInfo();
                        break;
                    case 4:
                        libraryController.takeFine();
                        break;
                    case 5:
                        libraryController.updateProfile();
                        break;
                    case 6 :
                        userController.getUsers();
                        break;
                    case 7:
                        userController.setLoggedInUserToNull();
                        loggedInUser = null;
                        System.out.println("✅ Logged out successfully!");
                        break;
                    default:
                        System.out.println("⚠️ Invalid choice. Please enter a number between 1 and 6.");
                }
            }
            else{
                System.out.println("\n===== Nexus Library - Welcome, " + loggedInUser.getFirstName() + " (" + loggedInUser.getRole() + ") =====");
                System.out.println("1. Borrow Book");
                System.out.println("2. Return Book");
                System.out.println("3. View Current Borrowings");
                System.out.println("4. View Borrowing History");
                System.out.println("5. Pay Fine");
                System.out.println("6. Search Book");
                System.out.println("7. Update Profile");
                System.out.println("8. Logout");

            }
        }
    }
}