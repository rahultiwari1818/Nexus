package com.Nexus_Library.model;

import java.sql.Timestamp;

public class Researcher extends User {
    public Researcher(int userId, String firstName, String lastName, String email, String password,
                      Timestamp registrationDate) {
        super(userId, firstName, lastName, email, password, registrationDate, "Researcher");
    }

    @Override
    public int getBorrowLimit() {
        return 10; // Researchers can borrow more
    }

    @Override
    public boolean canAccessResearchPapers() {
        return true; // Researchers need access
    }
}