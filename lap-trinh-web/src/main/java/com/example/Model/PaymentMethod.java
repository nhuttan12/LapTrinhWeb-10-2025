package com.example.Model;

public enum PaymentMethod {
    COD, BANK_TRANSFER, PAYPAL_TEST, MOMO_TEST, VNPAY_TEST;

    public String getMethod() {
        return name().toLowerCase();
    }

    public static PaymentMethod fromString(String type) {
        return PaymentMethod.valueOf(type.trim().toUpperCase());
    }
}

