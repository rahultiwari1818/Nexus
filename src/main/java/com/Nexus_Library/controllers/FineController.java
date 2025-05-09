package com.Nexus_Library.controllers;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.dao.FineDAO;
import com.Nexus_Library.model.User;
import com.Nexus_Library.utils.ValidationUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
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


}
