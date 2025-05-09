package com.Nexus_Library.model;

public class FineSetting {

    private int id;
    private String userType;
    private int finePerDay;
    private boolean activeStatus;

    public FineSetting() {
    }

    public FineSetting(int id, String userType, int finePerDay, boolean activeStatus) {
        this.id = id;
        this.userType = userType;
        this.finePerDay = finePerDay;
        this.activeStatus = activeStatus;
    }

    // Getters and Setters

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

    public int getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(int finePerDay) {
        this.finePerDay = finePerDay;
    }

    public boolean getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    @Override
    public String toString() {
        return "FineSetting{" +
                "id=" + id +
                ", userType='" + userType + '\'' +
                ", finePerDay=" + finePerDay +
                ", activeStatus='" + activeStatus + '\'' +
                '}';
    }
}
