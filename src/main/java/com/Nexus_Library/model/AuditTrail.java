package com.Nexus_Library.model;

import java.sql.Timestamp;

public class AuditTrail {

    private int auditId, resourceId, userId;
    private String actionType;
    private Timestamp timestamp;

    public AuditTrail(int auditId, int resourceId, int userId, String actionType, Timestamp timestamp) {
        this.auditId = auditId;
        this.resourceId = resourceId;
        this.userId = userId;
        this.actionType = actionType;
        this.timestamp = timestamp;
    }

    public int getAuditId() {
        return auditId;
    }

    public void setAuditId(int auditId) {
        this.auditId = auditId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


    // Getters and setters
}
