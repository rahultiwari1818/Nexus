package com.Nexus_Library.model;

import java.sql.Timestamp;

public class AccessLog {
    private int accessLogId;
    private int userId;
    private int resourceId;
    private Timestamp accessTime;
    private String accessType;

    public AccessLog(int accessLogId, int userId, int resourceId, Timestamp accessTime, String accessType) {
        this.accessLogId = accessLogId;
        this.userId = userId;
        this.resourceId = resourceId;
        this.accessTime = accessTime;
        this.accessType = accessType;
    }

    // Getters
    public int getAccessLogId() { return accessLogId; }
    public int getUserId() { return userId; }
    public int getResourceId() { return resourceId; }
    public Timestamp getAccessTime() { return accessTime; }
    public String getAccessType() { return accessType; }

    @Override
    public String toString() {
        return "AccessLog{accessLogId=" + accessLogId + ", userId=" + userId + ", resourceId=" + resourceId + "}";
    }
}

//package com.Nexus_Library.model;
//
//import java.sql.Timestamp;
//
//public class AccessLog {


//    private int accessLogId;
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public int getResourceId() {
//        return resourceId;
//    }
//
//    public void setResourceId(int resourceId) {
//        this.resourceId = resourceId;
//    }
//
//    public Timestamp getAccessTime() {
//        return accessTime;
//    }
//
//    public void setAccessTime(Timestamp accessTime) {
//        this.accessTime = accessTime;
//    }
//
//    public String getAccessType() {
//        return accessType;
//    }
//
//    public void setAccessType(String accessType) {
//        this.accessType = accessType;
//    }
//
//    private int userId;
//    private int resourceId;
//    private Timestamp accessTime;
//    private String accessType;
//
//    public AccessLog(int accessLogId, int userId, int resourceId, Timestamp accessTime, String accessType) {
//        this.accessLogId = accessLogId;
//        this.userId = userId;
//        this.resourceId = resourceId;
//        this.accessTime = accessTime;
//        this.accessType = accessType;
//    }
//
//
//    public int getAccessLogId() {
//        return accessLogId;
//    }
//
//    public void setAccessLogId(int accessLogId) {
//        this.accessLogId = accessLogId;
//    }
//
//    // Getters and setters
//}
