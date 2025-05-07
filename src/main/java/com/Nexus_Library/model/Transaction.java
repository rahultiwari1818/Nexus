package com.Nexus_Library.model;

import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private int userId;
    private int itemId;
    private String transactionType; // 'Borrow', 'Return', 'Fine'
    private Timestamp transactionDate;
    private Timestamp dueDate;
    private Timestamp returnDate;
    private String status; // 'Active', 'Completed', 'Overdue'

    // Constructor
    public Transaction(int transactionId, int userId, int itemId, String transactionType,
                       Timestamp transactionDate, Timestamp dueDate, Timestamp returnDate, String status) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.itemId = itemId;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public Timestamp getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction [transactionId=" + transactionId + ", userId=" + userId + ", itemId=" + itemId +
                ", transactionType=" + transactionType + ", transactionDate=" + transactionDate +
                ", dueDate=" + dueDate + ", returnDate=" + returnDate + ", status=" + status + "]";
    }
}