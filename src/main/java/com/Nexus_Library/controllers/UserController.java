package com.Nexus_Library.controllers;

import com.Nexus_Library.dao.UserDAO;
import com.Nexus_Library.model.User;
import com.Nexus_Library.pattern.creational.UserFactory;

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
        if (!isValidName(firstName)) {
            System.out.println("❌ First Name must contain only letters and spaces, and cannot be empty.");
            return false;
        }

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();
        if (!isValidName(lastName)) {
            System.out.println("❌ Last Name must contain only letters and spaces, and cannot be empty.");
            return false;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (!isValidEmail(email)) {
            System.out.println("❌ Invalid email format (e.g., must contain @ and .).");
            return false;
        }
        if (isEmailExists(email)) {
            System.out.println("❌ Email already registered.");
            return false;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        if (!isValidPassword(password)) {
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
        if (!isValidEmail(email)) {
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
        System.out.println("Enter Search Query : ");
        String query = scanner.nextLine();
        try {
            List<User> users = userDAO.getAllUsers(query);

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
                        usr.getRegistrationDate().toString());
            }

        } catch (Exception e) {
            System.out.println("❌ Error during login: " + e.getMessage());
        }
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUserToNull() {
        loggedInUser = null;
    }

    public void close() {
        scanner.close();
    }

    // Validation methods
    private boolean isValidName(String name) {
        return name != null && !name.isEmpty() && name.matches("[a-zA-Z\\s]+");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) return false;
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        return hasLetter && hasNumber;
    }

    private boolean isEmailExists(String email) {
        try {
            return userDAO.loginUser(email, "") != null;
        } catch (Exception e) {
            System.out.println("❌ Error checking email existence: " + e.getMessage());
            return false;
        }
    }
}