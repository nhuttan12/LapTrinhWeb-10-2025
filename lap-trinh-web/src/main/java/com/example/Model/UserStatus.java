package com.example.Model;

public enum UserStatus {
    ACTIVE, INACTIVE;

    public String getUserStatus() {
        return name().toLowerCase();
    }

    public static UserStatus fromString(String type) {
        return UserStatus.valueOf(type.trim().toUpperCase());
    }
}
