package com.Nexus_Library.controllers;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.User;
import com.Nexus_Library.utils.ValidationUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class FineController {

    public boolean payFine(User loggedInUser) {
        if (loggedInUser == null || !ValidationUtils.isValidRole(loggedInUser)) {
            System.out.println("❌ Please login as Student, Faculty, or Researcher to pay a fine.");
            return false;
        }


//                System.out.println("Outstanding Fine: $" + fineAmount + " for Transaction ID: " + transactionId);
//                System.out.print("Confirm payment (yes/no): ");
//                String confirm = scanner.nextLine().trim().toLowerCase();

        try {
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error paying fine: " + e.getMessage());
        }
        return false;
    }


}
