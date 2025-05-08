package com.Nexus_Library.app;

import com.Nexus_Library.model.User;
import com.Nexus_Library.pattern.structural.LibraryFacade;

import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LibraryFacade libraryFacade = new LibraryFacade();
        User loggedInUser = null;

        while (true) {
            if (loggedInUser == null) {
                System.out.println("\n===== Welcome to Nexus Library =====");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Search Book");
                System.out.println("4. Exit");
                System.out.print("Enter your choice (1-4): ");

                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println("❌ Invalid input. Please enter a number (1-4).");
                    continue;
                }

                switch (choice) {
                    case 1:
                        libraryFacade.registerUser();
                        break;
                    case 2:
                        loggedInUser = libraryFacade.loginUser();
                        if (loggedInUser != null) {
                            System.out.println("✅ Logged in as " + loggedInUser.getFirstName() + " (" + loggedInUser.getRole() + ")");
                        }
                        break;
                    case 3:
                        libraryFacade.searchBook();
                        break;
                    case 4:
                        System.out.println("👋 Exiting... Thank you for using Nexus Library!");
                        libraryFacade.close();
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("⚠️ Invalid choice. Please enter a number between 1 and 4.");
                }
            }

            else if ("Admin".equalsIgnoreCase(loggedInUser.getRole())) {
                System.out.println("\n=== Admin Menu ===");
                System.out.println("1. Add Library Item");
                System.out.println("2. Delete Book");
                System.out.println("3. Update Book Info");
                System.out.println("4. Take Fine");
                System.out.println("5. Update Profile");
                System.out.println("6. View Users");
                System.out.println("7. Logout");

                System.out.print("Enter your choice (1-7): ");
                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println("❌ Invalid input. Please enter a number between 1 and 7.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        libraryFacade.addLibraryItem(loggedInUser);
                        break;
                    case 2:
                        libraryFacade.deleteBook(loggedInUser);
                        break;
                    case 3:
                        libraryFacade.updateBook(loggedInUser);
                        break;
                    case 4:
                        libraryFacade.takeFine(loggedInUser);
                        break;
                    case 5:
                        loggedInUser = libraryFacade.updateProfile(loggedInUser);
                        break;
                    case 6:
                        libraryFacade.viewUsers();
                        break;
                    case 7:
                        loggedInUser = null;
                        System.out.println("✅ Logged out successfully!");
                        break;
                    default:
                        System.out.println("⚠️ Invalid choice. Please enter a number between 1 and 7.");
                }
            }

            else {
                System.out.println("\n===== Nexus Library - Welcome, " + loggedInUser.getFirstName() + " (" + loggedInUser.getRole() + ") =====");
                System.out.println("1. Borrow Book");
                System.out.println("2. Return Book");
                System.out.println("3. View Current Borrowings");
                System.out.println("4. View Borrowing History");
                System.out.println("5. Pay Fine");
                System.out.println("6. Search Book");
                System.out.println("7. Update Profile");
                System.out.println("8. Logout");

                System.out.print("Enter your choice (1-8): ");
                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println("❌ Invalid input. Please enter a number between 1 and 8.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        libraryFacade.borrowBook(loggedInUser);
                        break;
                    case 2:
                        libraryFacade.returnBook(loggedInUser);
                        break;
                    case 3:
                        libraryFacade.viewCurrentBorrowings(loggedInUser);
                        break;
                    case 4:
                        libraryFacade.viewBorrowingHistory(loggedInUser);
                        break;
                    case 5:
                        libraryFacade.payFine(loggedInUser);
                        break;
                    case 6:
                        libraryFacade.searchBook();
                        break;
                    case 7:
                        loggedInUser = libraryFacade.updateProfile(loggedInUser);
                        break;
                    case 8:
                        loggedInUser = null;
                        System.out.println("✅ Logged out successfully!");
                        break;
                    default:
                        System.out.println("⚠️ Invalid choice. Please enter a number between 1 and 8.");
                }
            }
        }
    }
}
