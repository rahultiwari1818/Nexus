package com.Nexus_Library.model;

import java.sql.Timestamp;

public class Faculty extends User {
    public Faculty(int userId, String firstName, String lastName, String email, String password,
                   Timestamp registrationDate) {
        super(userId, firstName, lastName, email, password, registrationDate, "Faculty");
    }


    @Override
    public boolean canAccessResearchPapers() {
        return true; // Faculty can access research papers
    }
}