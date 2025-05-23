package com.Nexus_Library.model;

import java.sql.Timestamp;

public class Student extends User {
    public Student(int userId, String firstName, String lastName, String email, String password,
                 Timestamp registrationDate) {
        super(userId, firstName, lastName, email, password, registrationDate, "Student");
    }


    @Override
    public boolean canAccessResearchPapers() {
        return false; // Guests cannot access research papers
    }
}