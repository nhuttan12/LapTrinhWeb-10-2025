package com.example.Model;

public enum OrderStatus {
    PENDING, PAID, SHIPPED, COMPLETED, CANCELLED;

    public String getOrderStatus() {
        return name().toLowerCase();
    }

    public static OrderStatus fromString(String type) {
        return OrderStatus.valueOf(type.trim().toUpperCase());
    }
}
