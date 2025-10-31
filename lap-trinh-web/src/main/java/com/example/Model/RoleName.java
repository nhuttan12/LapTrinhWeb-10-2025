package com.example.Model;

public enum RoleName {
    ADMIN,
    CUSTOMER;

    public String getRoleName() {
        return name().toLowerCase();
    }

    public static RoleName fromString(String type) {
        return RoleName.valueOf(type.trim().toUpperCase());
    }
}
