package com.example.Model;

public enum BrandStatus {
    ACTIVE, INACTIVE;

    public String getBrandStatus() {
        return name().toLowerCase();
    }

    public static BrandStatus fromString(String type) {
        return BrandStatus.valueOf(type.trim().toUpperCase());
    }
}
