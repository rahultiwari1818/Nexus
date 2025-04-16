package com.Nexus_Library.model;

public class Role {
    private int roleId;
    private String roleName;

    public Role(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    // Getters
    public int getRoleId() { return roleId; }
    public String getRoleName() { return roleName; }

    @Override
    public String toString() {
        return "Role{roleId=" + roleId + ", roleName='" + roleName + "'}";
    }
}

//package com.Nexus_Library.model;
//
//public class Role {
//    private int roleId;
//    private String roleName;
//
//    public Role(int roleId, String roleName) {
//        this.roleId = roleId;
//        this.roleName = roleName;
//    }
//
//    // Getters and setters
//
//    public int getRoleId() {
//        return roleId;
//    }
//
//    public void setRoleId(int roleId) {
//        this.roleId = roleId;
//    }
//
//    public String getRoleName() {
//        return roleName;
//    }
//
//    public void setRoleName(String roleName) {
//        this.roleName = roleName;
//    }
//}
