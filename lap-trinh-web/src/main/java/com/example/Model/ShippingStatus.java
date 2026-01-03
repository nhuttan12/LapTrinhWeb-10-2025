package com.example.Model;

public enum ShippingStatus {
    PENDING, SHIPPED, COMPLETED, CANCELLED;

    public String getStatus() {
        return name().toLowerCase();
    }

    public static ShippingStatus fromString(String status) {
        return ShippingStatus.valueOf(status.trim().toUpperCase());
    }
}
