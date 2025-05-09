package com.Nexus_Library.pattern.structural;

import com.Nexus_Library.controllers.*;
import com.Nexus_Library.model.User;

public class LibraryFacade {
    private final UserController userController;
    private final LibraryItemController libraryItemController;
    private final TransactionController transactionController;
    private final FineController fineController;
    private final FineSettingController fineSettingController;
    private final BorrowingSettingController borrowingSettingController;

    public LibraryFacade() {
        this.userController = new UserController();
        this.libraryItemController = new LibraryItemController();
        this.transactionController = new TransactionController();
        this.fineController = new FineController();
        this.fineSettingController = new FineSettingController();
        this.borrowingSettingController = new BorrowingSettingController();
    }

    // User-related operations
    public boolean registerUser() {
        return userController.register();
    }

    public User loginUser() {
        return userController.login();
    }

    public User updateProfile(User user) {
        return userController.updateProfile(user);
    }

    public void logout() {
        userController.close();
        libraryItemController.close();
    }

    public void viewUsers(User loggedInUser) {
        userController.getAllUsers(loggedInUser);
    }

    public void searchBook() {
        libraryItemController.searchLibraryItem();
    }

    public void searchUsers(User loggedInUser) {
        userController.searchUsers(loggedInUser);
    }

    // Admin-only: Library Item Management
    public void addLibraryItem(User admin) {
        libraryItemController.addItem(admin);
    }

    public void deleteBook(User admin) {
        libraryItemController.deleteBook(admin);
    }

    public void updateBook(User admin) {
        libraryItemController.updateBookInfo(admin);
    }

    public void viewAllLibraryItems() {
        libraryItemController.viewAllItems();
    }

    // Admin-only: Fine Setting Management
    public void addFineSetting(User admin) {
        fineSettingController.addFineSetting(admin);
    }

    public void removeFineSetting(User admin) {
        fineSettingController.deleteFineSetting(admin);
    }

    public void updateFineStatus(User admin) {
        fineSettingController.updateFineStatus(admin);
    }

    public void updateFineAmount(User admin) {
        fineSettingController.updateFineAmount(admin);
    }

    public void viewAllFineSettings(User admin) {
        fineSettingController.viewAllFineSettings(admin);
    }

    // Admin-only: Borrowing Setting Management
    public void addBorrowingSetting(User admin) {
        borrowingSettingController.addBorrowingSetting(admin);
    }

    public void updateBorrowingLimit(User admin) {
        borrowingSettingController.updateBorrowingLimit(admin);
    }

    public void updateBorrowingActiveStatus(User admin) {
        borrowingSettingController.updateActiveStatus(admin);
    }

    public void deleteBorrowingSetting(User admin) {
        borrowingSettingController.deleteBorrowingSetting(admin);
    }

    public void displayAllBorrowingSettings(User admin) {
        borrowingSettingController.displayAllBorrowingSettings(admin);
    }

    // Admin-only: Transaction Views
    public void viewAllTransactions(User admin) {
        transactionController.viewAllTransactions(admin);
    }

    public void viewAllCurrentBorrowings(User admin) {
        transactionController.viewAllActiveTransactions(admin);
    }

    // All Users: Book Borrowing Workflow
    public void borrowBook(User user) {
        transactionController.borrowBook(user);
    }

    public void returnBook(User user) {
        transactionController.returnBook(user);
    }

    public void viewCurrentBorrowings(User user) {
        transactionController.viewCurrentBorrowings(user);
    }

    public void viewBorrowingHistory(User user) {
        transactionController.viewBorrowingHistory(user);
    }

    public void payFine(User user) {
        fineController.payFine(user);
    }

    // Graceful shutdown
    public void close() {
        logout(); // uses logout logic to close necessary resources
    }
}
