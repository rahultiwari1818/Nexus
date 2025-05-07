package com.Nexus_Library.model;

import java.sql.Timestamp;

public class Fine {
    private int fineId;
    private int transactionId;
    private int userId;
    private double fineAmount;
    private Timestamp fineCalculatedDate;
    private String paymentStatus; // 'Pending', 'Paid', 'Waived'
    private Timestamp paymentDate;
    private Integer waivedBy; // Nullable, as it’s only set if payment_status is 'Waived'
    private String waivedReason; // Nullable

    // Constructor
    public Fine(int fineId, int transactionId, int userId, double fineAmount, Timestamp fineCalculatedDate,
                String paymentStatus, Timestamp paymentDate, Integer waivedBy, String waivedReason) {
        this.fineId = fineId;
        this.transactionId = transactionId;
        this.userId = userId;
        this.fineAmount = fineAmount;
        this.fineCalculatedDate = fineCalculatedDate;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.waivedBy = waivedBy;
        this.waivedReason = waivedReason;
    }

    // Getters and Setters
    public int getFineId() {
        return fineId;
    }

    public void setFineId(int fineId) {
        this.fineId = fineId;
    }

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

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public Timestamp getFineCalculatedDate() {
        return fineCalculatedDate;
    }

    public void setFineCalculatedDate(Timestamp fineCalculatedDate) {
        this.fineCalculatedDate = fineCalculatedDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getWaivedBy() {
        return waivedBy;
    }

    public void setWaivedBy(Integer waivedBy) {
        this.waivedBy = waivedBy;
    }

    public String getWaivedReason() {
        return waivedReason;
    }

    public void setWaivedReason(String waivedReason) {
        this.waivedReason = waivedReason;
    }

    @Override
    public String toString() {
        return "Fine [fineId=" + fineId + ", transactionId=" + transactionId + ", userId=" + userId +
                ", fineAmount=" + fineAmount + ", fineCalculatedDate=" + fineCalculatedDate +
                ", paymentStatus=" + paymentStatus + ", paymentDate=" + paymentDate +
                ", waivedBy=" + waivedBy + ", waivedReason=" + waivedReason + "]";
    }
}