package com.Nexus_Library.controllers;

import com.Nexus_Library.dao.FineSettingDAO;
import com.Nexus_Library.model.FineSetting;
import com.Nexus_Library.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class FineSettingController {

    private final FineSettingDAO fineSettingDAO;
    private final Scanner scanner;

    public FineSettingController() {
        this.fineSettingDAO = new FineSettingDAO();
        this.scanner = new Scanner(System.in);
    }

    public boolean addFineSetting(User loggedInUser) {
        try {
            if(loggedInUser == null || !"Admin".equalsIgnoreCase(loggedInUser.getRole())){
                System.out.println("❌ Only Admin can update book info.");
                return false;
            }
            System.out.print("Enter user type (e.g., Student, Faculty,Researcher): ");
            String userType = scanner.nextLine();

            System.out.print("Enter fine amount per day: ");
            int fineAmt = Integer.parseInt(scanner.nextLine());

            System.out.print("Is this setting active? (true/false): ");
            boolean status = scanner.nextLine().trim().equalsIgnoreCase("yes") ? true : false;

            FineSetting setting = fineSettingDAO.addNewFineSetting(userType, fineAmt, status);
            if (setting != null) {
                System.out.println("✅ Fine setting added successfully: ID = " + setting.getId());
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("❌ Failed to add fine setting: " + e.getMessage());
        }
        return false;
    }

    public boolean updateFineAmount(User loggedInUser) {
        try {
            if(loggedInUser == null || !"Admin".equalsIgnoreCase(loggedInUser.getRole())){
                System.out.println("❌ Only Admin can update book info.");
                return false;
            }

            System.out.print("Enter fine setting ID to update: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter new fine amount: ");
            int newAmt = Integer.parseInt(scanner.nextLine());

            boolean success = fineSettingDAO.updateFineSettingAmt(id, newAmt);
            if (success) {
                System.out.println("✅ Fine amount updated successfully.");
                return true;
            } else {
                System.out.println("❌ Failed to update fine amount.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ Error updating fine amount: " + e.getMessage());
            return false;
        }
    }

    public boolean updateFineStatus(User loggedInUser) {
        try {
            if(loggedInUser == null || !"Admin".equalsIgnoreCase(loggedInUser.getRole())){
                System.out.println("❌ Only Admin can update book info.");
                return false;
            }

            System.out.print("Enter fine setting ID to update status: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter new status (true/false): ");
            boolean status = scanner.nextLine().trim().equalsIgnoreCase("true") ? true : false;

            boolean success = fineSettingDAO.updateFineSettingActiveStatus(id, status);
            if (success) {
                System.out.println("✅ Status updated successfully.");
                return  true;
            } else {
                System.out.println("❌ Failed to update status.");
                return  false;
            }
        } catch (Exception e) {
            System.out.println("❌ Error updating status: " + e.getMessage());
            return  false;
        }
    }

    public boolean deleteFineSetting(User loggedInUser) {
        try {
            if(loggedInUser == null || !"Admin".equalsIgnoreCase(loggedInUser.getRole())){
                System.out.println("❌ Only Admin can update book info.");
                return false;
            }

            System.out.print("Enter fine setting ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine());

            boolean success = fineSettingDAO.deleteFineSetting(id);
            if (success) {
                System.out.println("🗑️ Fine setting deleted successfully.");
                return true;
            } else {
                System.out.println("❌ Failed to delete fine setting.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ Error deleting fine setting: " + e.getMessage());
            return false;
        }
    }

    public void viewAllFineSettings(User loggedInUser) {
        try {
            if(loggedInUser == null || !"Admin".equalsIgnoreCase(loggedInUser.getRole())){
                System.out.println("❌ Only Admin can update book info.");
                return ;
            }

            List<FineSetting> settings = fineSettingDAO.getAllFineSetting();

            if (settings.isEmpty()) {
                System.out.println("⚠️ No fine settings found.");
                return;
            }

            System.out.printf("%-5s %-15s %-15s %-10s%n", "ID", "User Type", "Fine/Day", "Status");
            System.out.println("--------------------------------------------------");

            for (FineSetting fs : settings) {
                System.out.printf("%-5d %-15s %-15d %-10s%n",
                        fs.getId(),
                        fs.getUserType(),
                        fs.getFinePerDay(),
                        fs.getActiveStatus());
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching fine settings: " + e.getMessage());
        }
    }
}
