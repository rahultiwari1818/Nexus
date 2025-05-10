package com.Nexus_Library.controllers;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.dao.FineDAO;
import com.Nexus_Library.model.Fine;
import com.Nexus_Library.model.User;
import com.Nexus_Library.utils.ValidationUtils;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class FineController {

    private final FineDAO fineDAO;
    private final Scanner scanner;

    public FineController(){
        this.fineDAO = new FineDAO();
        this.scanner = new Scanner(System.in);
    }

    public void payFine(User loggedInUser) {
        if (loggedInUser == null || !ValidationUtils.isValidRole(loggedInUser)) {
            System.out.println("❌ Please login as Student, Faculty, or Researcher to pay a fine.");
            return;
        }

        try {
            int totalFine = fineDAO.getOutstandingFineAmount(loggedInUser.getUserId());

            if (totalFine <= 0) {
                System.out.println("✅ You have no outstanding fines.");
                return;
            }

            System.out.println("💰 Outstanding Fine Total: ₹" + totalFine);
            System.out.println("Details of Pending Fines:");
            List<String> fineDetails = fineDAO.getPendingFinesDetails(loggedInUser.getUserId());
            fineDetails.forEach(System.out::println);

            System.out.print("Confirm payment of all fines (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();

            if ("yes".equals(confirm)) {
                boolean success = fineDAO.payAllPendingFines(loggedInUser.getUserId());
                if (success) {
                    System.out.println("✅ All fines paid successfully.");
                } else {
                    System.out.println("❌ Failed to update fine status.");
                }
            } else {
                System.out.println("❗ Payment cancelled.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error during fine payment: " + e.getMessage());
        }
    }

    public void viewAllFines(User loggedInUser) {
        if (loggedInUser == null || !"Admin".equalsIgnoreCase(loggedInUser.getRole())) {
            System.out.println("❌ Only Admin can view all fines.");
            return;
        }

        try {
            List<Fine> fines = fineDAO.getAllFines();
            if (fines.isEmpty()) {
                System.out.println("⚠️ No fines found.");
                return;
            }

            System.out.printf("%-8s %-10s %-10s %-10s %-20s %-15s %-20s %-10s %-15s%n",
                    "FineID", "UserID", "TxnID", "Amount", "Calc Date", "Status", "Payment Date", "WaivedBy", "Reason");

            for (Fine fine : fines) {
                System.out.printf("%-8d %-10d %-10d ₹%-9.2f %-20s %-15s %-20s %-10s %-15s%n",
                        fine.getFineId(),
                        fine.getUserId(),
                        fine.getTransactionId(),
                        fine.getFineAmount(),
                        fine.getFineCalculatedDate() != null ? fine.getFineCalculatedDate().toString() : "N/A",
                        fine.getPaymentStatus(),
                        fine.getPaymentDate() != null ? fine.getPaymentDate().toString() : "N/A",
                        fine.getWaivedBy() != null ? fine.getWaivedBy().toString() : "N/A",
                        fine.getWaivedReason() != null ? fine.getWaivedReason() : "N/A"
                );
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching fine records: " + e.getMessage());
        }
    }


    public void viewAllPendingFines(User loggedInUser) {
        if (loggedInUser == null || !"Admin".equalsIgnoreCase(loggedInUser.getRole())) {
            System.out.println("❌ Only Admin can view all pending fines.");
            return;
        }

        try {
            List<Fine> pendingFines = fineDAO.getAllPendingFines();
            if (pendingFines.isEmpty()) {
                System.out.println("✅ No pending fines found.");
            } else {
                System.out.println("\n----- Pending Fines -----");
                System.out.printf("%-8s %-10s %-10s %-10s %-20s %-15s %-20s %-10s %-15s%n",
                        "FineID", "UserID", "TxnID", "Amount", "Calc Date", "Status", "Payment Date", "WaivedBy", "Reason");

                for (Fine fine : pendingFines) {
                    System.out.printf("%-8d %-10d %-10d ₹%-9.2f %-20s %-15s %-20s %-10s %-15s%n",
                            fine.getFineId(),
                            fine.getUserId(),
                            fine.getTransactionId(),
                            fine.getFineAmount(),
                            fine.getFineCalculatedDate() != null ? fine.getFineCalculatedDate().toString() : "N/A",
                            fine.getPaymentStatus(),
                            fine.getPaymentDate() != null ? fine.getPaymentDate().toString() : "N/A",
                            fine.getWaivedBy() != null ? fine.getWaivedBy().toString() : "N/A",
                            fine.getWaivedReason() != null ? fine.getWaivedReason() : "N/A"
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error retrieving pending fines: " + e.getMessage());
        }
    }



}
