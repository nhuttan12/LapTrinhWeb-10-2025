package com.example.Model;

public enum RoleStatus {
    ACTIVE, INACTIVE;

    public String getRoleStatus() {
        return name().toLowerCase();
    }

    public static RoleStatus fromString(String type) {
        return RoleStatus.valueOf(type.trim().toUpperCase());
    }
}
