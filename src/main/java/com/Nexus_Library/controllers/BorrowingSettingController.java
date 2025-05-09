package com.Nexus_Library.controllers;

import com.Nexus_Library.dao.BorrowingSettingDAO;
import com.Nexus_Library.model.BorrowingSetting;
import com.Nexus_Library.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BorrowingSettingController {

    private final BorrowingSettingDAO borrowingSettingDAO = new BorrowingSettingDAO();
    private final Scanner scanner = new Scanner(System.in);

    // Method to add a new borrowing setting
    public void addBorrowingSetting(User loggedInUser) {
        if (!"Admin".equals(loggedInUser.getRole())) {
            System.out.println("❌ Only Admin can add borrowing settings.");
            return;
        }

        System.out.print("Enter user type (e.g., Student, Faculty, Researcher9): ");
        String userType = scanner.nextLine().trim();

        System.out.print("Enter borrowing limit: ");
        int limit;
        try {
            limit = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid limit. Please enter a valid number.");
            return;
        }

        System.out.print("Is the setting active? (true/false): ");
        boolean active;
        try {
            active = Boolean.parseBoolean(scanner.nextLine().trim());
        } catch (Exception e) {
            System.out.println("❌ Invalid status. Please enter true or false.");
            return;
        }

        try {
            BorrowingSetting setting = borrowingSettingDAO.addBorrowingSetting(userType, limit, active);
            if (setting != null) {
                System.out.println("✅ Borrowing setting added successfully.");
            } else {
                System.out.println("❌ Failed to add borrowing setting.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error while adding borrowing setting: " + e.getMessage());
        }
    }

    // Method to update the borrowing limit for a specific setting
    public void updateBorrowingLimit(User loggedInUser) {
        if (!"Admin".equals(loggedInUser.getRole())) {
            System.out.println("❌ Only Admin can update borrowing limits.");
            return;
        }

        System.out.print("Enter borrowing setting ID to update: ");
        int settingId;
        try {
            settingId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid setting ID.");
            return;
        }

        System.out.print("Enter new borrowing limit: ");
        int newLimit;
        try {
            newLimit = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid limit.");
            return;
        }

        try {
            boolean success = borrowingSettingDAO.updateBorrowingLimit(settingId, newLimit);
            if (success) {
                System.out.println("✅ Borrowing limit updated successfully.");
            } else {
                System.out.println("❌ Failed to update borrowing limit.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error while updating borrowing limit: " + e.getMessage());
        }
    }

    // Method to update the active status for a borrowing setting
    public void updateActiveStatus(User loggedInUser) {
        if (!"Admin".equals(loggedInUser.getRole())) {
            System.out.println("❌ Only Admin can update borrowing settings.");
            return;
        }

        System.out.print("Enter borrowing setting ID to update: ");
        int settingId;
        try {
            settingId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid setting ID.");
            return;
        }

        System.out.print("Enter new active status (true/false): ");
        boolean newStatus;
        try {
            newStatus = Boolean.parseBoolean(scanner.nextLine().trim());
        } catch (Exception e) {
            System.out.println("❌ Invalid status. Please enter true or false.");
            return;
        }

        try {
            boolean success = borrowingSettingDAO.updateActiveStatus(settingId, newStatus);
            if (success) {
                System.out.println("✅ Borrowing setting status updated successfully.");
            } else {
                System.out.println("❌ Failed to update borrowing setting status.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error while updating active status: " + e.getMessage());
        }
    }

    // Method to delete a borrowing setting by ID
    public void deleteBorrowingSetting(User loggedInUser) {
        if (!"Admin".equals(loggedInUser.getRole())) {
            System.out.println("❌ Only Admin can delete borrowing settings.");
            return;
        }

        System.out.print("Enter borrowing setting ID to delete: ");
        int settingId;
        try {
            settingId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid setting ID.");
            return;
        }

        try {
            boolean success = borrowingSettingDAO.deleteBorrowingSetting(settingId);
            if (success) {
                System.out.println("✅ Borrowing setting deleted successfully.");
            } else {
                System.out.println("❌ Failed to delete borrowing setting.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error while deleting borrowing setting: " + e.getMessage());
        }
    }

    // Method to display all borrowing settings
        public void displayAllBorrowingSettings(User loggedInUser) {
        if (!"Admin".equals(loggedInUser.getRole())) {
            System.out.println("❌ Only Admin can view borrowing settings.");
            return;
        }

        try {
            List<BorrowingSetting> settings = borrowingSettingDAO.getAllBorrowingSettings();
            if (settings.isEmpty()) {
                System.out.println("❌ No borrowing settings found.");
            } else {
                System.out.println("\n===== Borrowing Settings =====");
                for (BorrowingSetting setting : settings) {
                    System.out.println("ID: " + setting.getId() + ", User Type: " + setting.getUserType() +
                            ", Limit: " + setting.getBorrowingLimit() + ", Active: " + setting.isActive());
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error while fetching borrowing settings: " + e.getMessage());
        }
    }

    // Method to get max borrowing limit for the logged in user
    public void getMaxBorrowLimit(User loggedInUser) {
        try {
            int maxLimit = borrowingSettingDAO.getMaxBorrowPerUserType(loggedInUser);
            System.out.println("Max Borrow Limit for " + loggedInUser.getRole() + ": " + maxLimit);
        } catch (SQLException e) {
            System.out.println("❌ Error fetching borrowing limit for user: " + e.getMessage());
        }
    }
}
