package com.example.Model;

public enum ShippingStatus {
    PENDING, SHIPPED, COMPLETED, CANCELLED;

    public String getOrderStatus() {
        return name().toLowerCase();
    }

    public static ShippingStatus fromString(String value) {
        if (value == null) throw new IllegalArgumentException("ShippingStatus cannot be null");
        switch (value.trim().toLowerCase()) {
            case "pending":   return PENDING;
            case "shipped":   return SHIPPED;
            case "completed": return COMPLETED;
            case "cancelled": return CANCELLED;
            default: throw new IllegalArgumentException("Unknown ShippingStatus: " + value);
        }
    }
}
