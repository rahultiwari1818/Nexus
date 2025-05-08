package com.Nexus_Library.controllers;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.dao.LibraryItemDAO;
import com.Nexus_Library.model.LibraryItem;
import com.Nexus_Library.model.User;
import com.Nexus_Library.pattern.behavioral.AuthorSearch;
import com.Nexus_Library.pattern.behavioral.IsbnSearch;
import com.Nexus_Library.pattern.behavioral.SearchContext;
import com.Nexus_Library.pattern.behavioral.TitleSearch;
import com.Nexus_Library.pattern.creational.LibraryItemFactory;
import com.Nexus_Library.utils.ValidationUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;
import java.util.regex.Pattern;

public class LibraryItemController {
    private final LibraryItemDAO itemDAO;
    private final Scanner scanner;

    public LibraryItemController() {
        this.itemDAO = new LibraryItemDAO();
        this.scanner = new Scanner(System.in);
    }


    public boolean addItem(User loggedInUser) {
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
        if (!ValidationUtils.isValidName(author)) {
            System.out.println("❌ Author must contain only letters and spaces, and cannot be empty.");
            return false;
        }

        System.out.print("ISBN (13 digits): ");
        String isbn = scanner.nextLine().trim();
        if (!ValidationUtils.isValidIsbn(isbn)) {
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


    public void searchLibraryItem() {
        System.out.println("Select search type:");
        System.out.println("1. Title");
        System.out.println("2. Author");
        System.out.println("3. ISBN");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid choice.");
            return;
        }

        System.out.print("Enter Search Query: ");
        String query = scanner.nextLine();

        SearchContext<LibraryItem> context = new SearchContext<>();

        switch (choice) {
            case 1:
                context.setStrategy(new TitleSearch());
                break;
            case 2:
                context.setStrategy(new AuthorSearch());
                break;
            case 3:
                context.setStrategy(new IsbnSearch());
                break;
            default:
                System.out.println("❌ Invalid option.");
                return;
        }

        try {
            List<LibraryItem> items = context.executeSearch(query);

            if (items == null || items.isEmpty()) {
                System.out.println("⚠️ No items found matching your query.");
                return;
            }

            System.out.printf("%-5s %-30s %-20s %-15s %-12s %-20s %-15s%n",
                    "ID", "Title", "Author", "ISBN", "Available", "Added Date", "Type");
            System.out.println("--------------------------------------------------------------------------------------------------------");

            for (LibraryItem item : items) {
                System.out.printf("%-5d %-30s %-20s %-15s %-12s %-20s %-15s%n",
                        item.getItemId(),
                        item.getTitle(),
                        item.getAuthor() != null ? item.getAuthor() : "N/A",
                        item.getIsbn() != null ? item.getIsbn() : "N/A",
                        item.isAvailable() ? "Yes" : "No",
                        item.getAddedDate().toString(),
                        item.getItemType());
            }

        } catch (Exception e) {
            System.out.println("❌ An error occurred while searching for library items.");
            e.printStackTrace();
        }
    }






    public void close() {
        scanner.close();
    }
}