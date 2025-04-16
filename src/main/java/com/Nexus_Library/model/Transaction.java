package com.Nexus_Library.model;

import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private int userId;
    private int loanId;
    private int fineId;
    private String transactionType;
    private double amount;
    private Timestamp transactionDate;
    private String status;

    public Transaction(int transactionId, int userId, int loanId, int fineId, String transactionType, double amount, Timestamp transactionDate, String status) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.loanId = loanId;
        this.fineId = fineId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.status = status;
    }

    // Getters
    public int getTransactionId() { return transactionId; }
    public int getUserId() { return userId; }
    public int getLoanId() { return loanId; }
    public int getFineId() { return fineId; }
    public String getTransactionType() { return transactionType; }
    public double getAmount() { return amount; }
    public Timestamp getTransactionDate() { return transactionDate; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "Transaction{transactionId=" + transactionId + ", userId=" + userId + ", amount=" + amount + "}";
    }
}