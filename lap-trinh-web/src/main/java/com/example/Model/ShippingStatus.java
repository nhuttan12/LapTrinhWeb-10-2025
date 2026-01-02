package com.example.Model;

public enum ShippingStatus {
    PENDING, SHIPPED, COMPLETED, CANCELLED;

    public String getShippingStatus() {
        return name().toLowerCase();
    }

    public static ShippingStatus fromString(String type) {
        return ShippingStatus.valueOf(type.trim().toUpperCase());
    }
}
