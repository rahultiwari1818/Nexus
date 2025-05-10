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
                System.out.println("4. View All Book");
                System.out.println("5. Exit");
                System.out.print("Enter your choice (1-5): ");

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
                            System.out.println("-------------------------------------✅ Logged in as " + loggedInUser.getFirstName() + " (" + loggedInUser.getRole() + ") --------------------------------------------------");
                        }
                        break;
                    case 3:
                        libraryFacade.searchBook();
                        break;
                    case 4:
                        libraryFacade.viewAllLibraryItems();
                        break;
                    case 5:
                        System.out.println("--------------------------------------------------👋 Exiting... Thank you for using Nexus Library!-----------------------------------");
                        libraryFacade.close();
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("⚠️ Invalid choice. Please enter a number between 1 and 4.");
                }
            }

            // ------------------ Admin Menu ------------------ //
            else if ("Admin".equalsIgnoreCase(loggedInUser.getRole())) {
                System.out.println("\n===== Admin Menu =====");
                System.out.println("1. Add Library Item");
                System.out.println("2. Delete Library Item");
                System.out.println("3. Update Library Item");
                System.out.println("4. View All Library Items");
                System.out.println("5. Add Fine Setting");
                System.out.println("6. Remove Fine Setting");
                System.out.println("7. Update Fine Status");
                System.out.println("8. Update Fine Amount");
                System.out.println("9. Add Borrowing Setting");
                System.out.println("10. Update Borrowing Limit");
                System.out.println("11. Update Borrowing Active Status");
                System.out.println("12. Remove Borrowing Setting");
                System.out.println("13. Display All Borrowing Settings");
                System.out.println("14. View All Transactions");
                System.out.println("15. View Current Borrowings");
                System.out.println("16. View All Users");
                System.out.println("17. Search Books");
                System.out.println("18. Search Users");
                System.out.println("19. View Fine Settings");
                System.out.println("20. Update Profile");
                System.out.println("21. View ALl Pending Fines ");
                System.out.println("22. View All Fines ");
                System.out.println("23. Logout");
                System.out.print("Enter your choice (1-23): ");

                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println("❌ Invalid input. Please enter a number between 1 and 21.");
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
                        libraryFacade.viewAllLibraryItems();
                        break;
                    case 5:
                        libraryFacade.addFineSetting(loggedInUser);
                        break;
                    case 6:
                        libraryFacade.removeFineSetting(loggedInUser);
                        break;
                    case 7:
                        libraryFacade.updateFineStatus(loggedInUser);
                        break;
                    case 8:
                        libraryFacade.updateFineAmount(loggedInUser);
                        break;
                    case 9:
                        libraryFacade.addBorrowingSetting(loggedInUser);
                        break;
                    case 10:
                        libraryFacade.updateBorrowingLimit(loggedInUser);
                        break;
                    case 11:
                        libraryFacade.updateBorrowingActiveStatus(loggedInUser);
                        break;
                    case 12:
                        libraryFacade.deleteBorrowingSetting(loggedInUser);
                        break;
                    case 13:
                        libraryFacade.displayAllBorrowingSettings(loggedInUser);
                        break;
                    case 14:
                        libraryFacade.viewAllTransactions(loggedInUser);
                        break;
                    case 15:
                        libraryFacade.viewAllCurrentBorrowings(loggedInUser);
                        break;
                    case 16:
                        libraryFacade.viewUsers(loggedInUser);
                        break;
                    case 17:
                        libraryFacade.searchBook();
                        break;
                    case 18:
                        libraryFacade.searchUsers(loggedInUser);
                        break;
                    case 19:
                        libraryFacade.viewAllFineSettings(loggedInUser);
                        break;
                    case 20:
                        loggedInUser = libraryFacade.updateProfile(loggedInUser);
                        break;
                    case 21:
                        libraryFacade.viewAllPendingFines(loggedInUser);
                        break;
                    case 22:
                        libraryFacade.viewAllFines(loggedInUser);
                        break;
                    case 23:
                        loggedInUser = null;
//                        libraryFacade.logout();
                        System.out.println("✅ Logged out successfully!");
                        break;
                    default:
                        System.out.println("⚠️ Invalid choice. Please enter a number between 1 and 21.");
                }
            }

            // ------------------ Student / Faculty / Researcher Menu ------------------ //
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
