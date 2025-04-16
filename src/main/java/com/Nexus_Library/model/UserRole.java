package com.Nexus_Library.model;

public class UserRole {
    private int userId;
    private int roleId;

    public UserRole(int userId, int roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    // Getters
    public int getUserId() { return userId; }
    public int getRoleId() { return roleId; }

    @Override
    public String toString() {
        return "UserRole{userId=" + userId + ", roleId=" + roleId + "}";
    }
}

//package com.Nexus_Library.model;
//
//public class UserRole {
//    private int userId, roleId;
//
//    public UserRole(int userId, int roleId) {
//        this.userId = userId;
//        this.roleId = roleId;
//    }
//
//    // Getters and setters
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public int getRoleId() {
//        return roleId;
//    }
//
//    public void setRoleId(int roleId) {
//        this.roleId = roleId;
//    }
//}
