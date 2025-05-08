package com.Nexus_Library.controllers;

import com.Nexus_Library.dao.UserDAO;
import com.Nexus_Library.model.User;
import com.Nexus_Library.pattern.behavioral.*;
import com.Nexus_Library.pattern.creational.UserFactory;
import com.Nexus_Library.utils.ValidationUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserController {
    private final UserDAO userDAO;
    private final Scanner scanner;
    private User loggedInUser; // Track the logged-in user

    public UserController() {
        this.userDAO = new UserDAO();
        this.scanner = new Scanner(System.in);
    }

    public boolean register() {
        System.out.println("\n=== User Registration ===");

        System.out.print("First Name: ");
        String firstName = scanner.nextLine().trim();
        if (!ValidationUtils.isValidName(firstName)) {
            System.out.println("❌ First Name must contain only letters and spaces, and cannot be empty.");
            return false;
        }

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();
        if (!ValidationUtils.isValidName(lastName)) {
            System.out.println("❌ Last Name must contain only letters and spaces, and cannot be empty.");
            return false;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (!ValidationUtils.isValidEmail(email)) {
            System.out.println("❌ Invalid email format (e.g., must contain @ and .).");
            return false;
        }
        if (ValidationUtils.isEmailExists(userDAO,email)) {
            System.out.println("❌ Email already registered.");
            return false;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        if (!ValidationUtils.isValidPassword(password)) {
            System.out.println("❌ Password must be at least 6 characters long and include at least one letter and one number.");
            return false;
        }

        String[] validRoles = {"faculty", "student", "researcher"};
        System.out.print("Role (" + String.join(", ", validRoles) + "): ");
        String role = scanner.nextLine().trim();
        if (role.isEmpty() || !Arrays.asList(validRoles).contains(role.toLowerCase())) {
            System.out.println("❌ Invalid role. Please choose from: " + String.join(", ", validRoles));
            return false;
        }


        User user = UserFactory.createUser(role, 0, firstName, lastName, email, password, new Timestamp(System.currentTimeMillis()));


        try {
            boolean success = userDAO.registerUser(user);
            if (success) {
                System.out.println("✅ Registration successful! User ID: " + user.getUserId());
            } else {
                System.out.println("❌ Registration failed.");
            }
            return success;
        } catch (Exception e) {
            System.out.println("❌ Error during registration: " + e.getMessage());
            return false;
        }
    }

    public User login() {
        System.out.println("\n=== User Login ===");

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("❌ Email cannot be empty.");
            return null;
        }
        if (!ValidationUtils.isValidEmail(email)) {
            System.out.println("❌ Invalid email format (e.g., must contain @ and .).");
            return null;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        if (password.isEmpty()) {
            System.out.println("❌ Password cannot be empty.");
            return null;
        }

        try {
            User loggedInUser = userDAO.loginUser(email, password);
            if (loggedInUser != null) {
                this.loggedInUser = loggedInUser; // Set the logged-in user
                System.out.println("✅ Login successful! Welcome, " + loggedInUser.getFirstName() +
                        " (" + loggedInUser.getRole() + ")!");
                System.out.println("Borrow Limit: " + loggedInUser.getBorrowLimit() +
                        ", Research Access: " + loggedInUser.canAccessResearchPapers());
                return loggedInUser;
            } else {
                System.out.println("❌ Invalid email or password.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("❌ Error during login: " + e.getMessage());
            return null;
        }
    }

    public void getUsers() {
        System.out.println("Choose search type:");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Email");
        System.out.println("3. Search by Role");

        String input = scanner.nextLine().trim();
        SearchStrategy<User> strategy = null;

        switch (input) {
            case "1":
                strategy = new NameSearch();
                break;
            case "2":
                strategy = new EmailSearch();
                break;
            case "3":
                strategy = new RoleSearch();
                break;
            default:
                System.out.println("❌ Invalid choice.");
                return;
        }

        System.out.print("Enter Search Query: ");
        String query = scanner.nextLine().trim();

        try {
            SearchContext<User> context = new SearchContext<>();
            context.setStrategy(strategy);
            List<User> users = context.executeSearch(query);

            if (users == null || users.isEmpty()) {
                System.out.println("⚠️ No users found.");
                return;
            }

            System.out.printf("%-5s %-15s %-15s %-30s %-20s %-20s%n",
                    "ID", "First Name", "Last Name", "Email", "Role", "Registered");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------");

            for (User usr : users) {
                System.out.printf("%-5d %-15s %-15s %-30s %-20s %-20s%n",
                        usr.getUserId(),
                        usr.getFirstName(),
                        usr.getLastName(),
                        usr.getEmail(),
                        usr.getRole(),
                        usr.getRegistrationDate() != null ? usr.getRegistrationDate().toString() : "N/A");
            }

        } catch (Exception e) {
            System.out.println("❌ Error during search: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public User updateProfile(User user) {
        System.out.println("=== Update Profile ===");

        System.out.print("First Name (" + user.getFirstName() + "): ");
        String firstName = scanner.nextLine().trim();
        if (!firstName.isEmpty()) user.setFirstName(firstName);

        System.out.print("Last Name (" + user.getLastName() + "): ");
        String lastName = scanner.nextLine().trim();
        if (!lastName.isEmpty()) user.setLastName(lastName);

        System.out.print("Email (" + user.getEmail() + "): ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) user.setEmail(email);

        System.out.print("Password (hidden): ");
        String password = scanner.nextLine().trim();
        if (!password.isEmpty()) user.setPassword(password);

        try {

            User updatedUser = userDAO.updateProfile(user);
            if (updatedUser != null) {
                System.out.println("✅ Profile updated successfully!");
            } else {
                System.out.println("❌ Failed to update profile.");
            }
            return updatedUser;
        } catch (Exception e) {
            System.out.println("❌ Error during search: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }






    public void close() {
        scanner.close();
    }


}