package com.Nexus_Library.model;

import java.sql.Timestamp;

public class Notification {
    private int notificationId;
    private int userId;
    private String notificationType;
    private String message;
    private Timestamp sentAt;
    private String status;
    private String deliveryMethod;

    public Notification(int notificationId, int userId, String notificationType, String message, Timestamp sentAt, String status, String deliveryMethod) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.notificationType = notificationType;
        this.message = message;
        this.sentAt = sentAt;
        this.status = status;
        this.deliveryMethod = deliveryMethod;
    }

    // Getters
    public int getNotificationId() { return notificationId; }
    public int getUserId() { return userId; }
    public String getNotificationType() { return notificationType; }
    public String getMessage() { return message; }
    public Timestamp getSentAt() { return sentAt; }
    public String getStatus() { return status; }
    public String getDeliveryMethod() { return deliveryMethod; }

    @Override
    public String toString() {
        return "Notification{notificationId=" + notificationId + ", userId=" + userId + ", message='" + message + "'}";
    }
}