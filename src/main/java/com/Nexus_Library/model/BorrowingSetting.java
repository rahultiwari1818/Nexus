package com.Nexus_Library.model;

public class BorrowingSetting {
    private int id;
    private String userType;
    private int borrowingLimit;
    private boolean active;

    public BorrowingSetting() {}

    public BorrowingSetting(int id, String userType, int borrowingLimit, boolean active) {
        this.id = id;
        this.userType = userType;
        this.borrowingLimit = borrowingLimit;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getBorrowingLimit() {
        return borrowingLimit;
    }

    public void setBorrowingLimit(int borrowingLimit) {
        this.borrowingLimit = borrowingLimit;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
