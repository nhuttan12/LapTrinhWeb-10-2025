package com.example.Model;

public enum ProductStatus {
    ACTIVE, INACTIVE;

    public String getProductStatus() {
        return name().toLowerCase();
    }

    public static ProductStatus fromString(String type) {
        return ProductStatus.valueOf(type.trim().toUpperCase());
    }
}
