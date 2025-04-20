package com.Nexus_Library.model;

import java.sql.Timestamp;

public abstract class User {
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password; // Should be hashed in production
    private Timestamp registrationDate;
    private String role;

    public User(int userId, String firstName, String lastName, String email, String password,
                Timestamp registrationDate, String role) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.role = role;
    }

    // Getters and setters (Encapsulation)
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Timestamp getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Timestamp registrationDate) { this.registrationDate = registrationDate; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Abstract methods for Polymorphism
    public abstract int getBorrowLimit(); // Different limits per role
    public abstract boolean canAccessResearchPapers(); // Role-based access
}