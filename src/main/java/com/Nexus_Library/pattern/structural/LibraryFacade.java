package com.Nexus_Library.pattern.structural;

import com.Nexus_Library.controllers.*;
import com.Nexus_Library.model.User;

public class LibraryFacade {
    private final UserController userController;
    private final LibraryItemController libraryItemController;
    private final LibraryController libraryController;
    private final TransactionController transactionController;
    private final FineController fineController;

    public LibraryFacade() {
        this.userController = new UserController();
        this.libraryItemController = new LibraryItemController();
        this.libraryController = new LibraryController();
        this.transactionController = new TransactionController();
        this.fineController = new FineController();
    }

    // User-related operations
    public boolean registerUser() {
        return userController.register();
    }

    public User loginUser() {
        return userController.login();
    }

    public User updateProfile(User user) {
        libraryController.updateProfile(user);
    }

    public void logout() {
        userController.close();
        libraryItemController.close();
    }

    public void viewUsers() {
        userController.getUsers();
    }

    // Admin-only operations
    public void addLibraryItem(User admin) {
        libraryItemController.addItem(admin);
    }

    public void deleteBook(User admin) {
        libraryController.deleteBook(admin);
    }

    public void updateBook(User admin) {
        libraryController.updateBookInfo(admin);
    }

    public void takeFine(User admin) {
        libraryController.takeFine(admin);
    }

    // Common to all users
    public void searchBook() {
        libraryItemController.searchLibraryItem();
    }

    // User borrowing operations
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

    // Close all resources gracefully
    public void close() {
        logout(); // reuse logout logic to close resources
    }
}
