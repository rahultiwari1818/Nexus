package com.Nexus_Library.model;

public class Fine {
    private int fineId, loanId;
    private double fineAmount;
    private String fineStatus, paymentStatus;

    public Fine(int fineId, int loanId, double fineAmount, String fineStatus, String paymentStatus) {
        this.fineId = fineId;
        this.loanId = loanId;
        this.fineAmount = fineAmount;
        this.fineStatus = fineStatus;
        this.paymentStatus = paymentStatus;
    }

    // Getters and setters

    public int getFineId() {
        return fineId;
    }

    public void setFineId(int fineId) {
        this.fineId = fineId;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getFineStatus() {
        return fineStatus;
    }

    public void setFineStatus(String fineStatus) {
        this.fineStatus = fineStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
