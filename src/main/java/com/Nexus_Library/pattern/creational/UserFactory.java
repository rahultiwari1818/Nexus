package com.Nexus_Library.pattern.creational;

import com.Nexus_Library.model.*;
import java.sql.Timestamp;

public class UserFactory {
    public static User createUser(String role, int userId, String firstName, String lastName, String email,
                                  String password, Timestamp registrationDate) {
        switch (role.toLowerCase()) {
            case "faculty":
                return new Faculty(userId, firstName, lastName, email, password, registrationDate);
            case "student":
                return new Student(userId, firstName, lastName, email, password, registrationDate);
            case "admin":
                return new Admin(userId, firstName, lastName, email, password, registrationDate);
            case "researcher":
                return new Researcher(userId, firstName, lastName, email, password, registrationDate);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}