package com.Nexus_Library.model;

import java.sql.Timestamp;

public class Loan {
    private int loanId;
    private int userId;
    private int resourceId;
    private Timestamp borrowDate;
    private Timestamp dueDate;
    private Timestamp returnDate;
    private String status;

    public Loan(int loanId, int userId, int resourceId, Timestamp borrowDate, Timestamp dueDate, Timestamp returnDate, String status) {
        this.loanId = loanId;
        this.userId = userId;
        this.resourceId = resourceId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getters
    public int getLoanId() { return loanId; }
    public int getUserId() { return userId; }
    public int getResourceId() { return resourceId; }
    public Timestamp getBorrowDate() { return borrowDate; }
    public Timestamp getDueDate() { return dueDate; }
    public Timestamp getReturnDate() { return returnDate; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "Loan{loanId=" + loanId + ", userId=" + userId + ", resourceId=" + resourceId + "}";
    }
}