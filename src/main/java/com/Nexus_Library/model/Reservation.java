package com.Nexus_Library.model;

import java.sql.Timestamp;
import java.sql.Date;

public class Reservation {
    private int reservationId, userId, resourceId;
    private Timestamp reservationDate;
    private Date pickupDate;
    private String status;

    public Reservation(int reservationId, int userId, int resourceId, Timestamp reservationDate, Date pickupDate, String status) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.resourceId = resourceId;
        this.reservationDate = reservationDate;
        this.pickupDate = pickupDate;
        this.status = status;
    }

    // Getters and setters

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public Timestamp getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Timestamp reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
