package com.example.Model;

public enum PaymentStatus {

    PENDING, COMPLETED, FAILED, REFUNDED, UNPAID, PAID;

    //    PENDING, COMPLETED, FAILED, REFUNDED;
    public String getStatus() {
        return name().toLowerCase();
    }

    public static PaymentStatus fromString(String status) {
        return PaymentStatus.valueOf(status.trim().toUpperCase());
    }
}
