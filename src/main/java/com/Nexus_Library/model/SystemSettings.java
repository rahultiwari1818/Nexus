package com.Nexus_Library.model;

public class SystemSettings {
    private int settingId;
    private int maxBorrowLimit, maxReservationTime;
    private double fineRate;
    private String systemStatus;

    public SystemSettings(int settingId, int maxBorrowLimit, double fineRate, int maxReservationTime, String systemStatus) {
        this.settingId = settingId;
        this.maxBorrowLimit = maxBorrowLimit;
        this.fineRate = fineRate;
        this.maxReservationTime = maxReservationTime;
        this.systemStatus = systemStatus;
    }

    // Getters and setters

    public int getSettingId() {
        return settingId;
    }

    public void setSettingId(int settingId) {
        this.settingId = settingId;
    }

    public int getMaxBorrowLimit() {
        return maxBorrowLimit;
    }

    public void setMaxBorrowLimit(int maxBorrowLimit) {
        this.maxBorrowLimit = maxBorrowLimit;
    }

    public int getMaxReservationTime() {
        return maxReservationTime;
    }

    public void setMaxReservationTime(int maxReservationTime) {
        this.maxReservationTime = maxReservationTime;
    }

    public double getFineRate() {
        return fineRate;
    }

    public void setFineRate(double fineRate) {
        this.fineRate = fineRate;
    }

    public String getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(String systemStatus) {
        this.systemStatus = systemStatus;
    }
}
