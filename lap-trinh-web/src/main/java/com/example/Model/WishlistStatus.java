package com.example.Model;

public enum WishlistStatus {
    ACTIVE, INACTIVE;

    public String getWishlistStatus() {
        return name().toLowerCase();
    }

    public static WishlistStatus fromString(String type) {
        return WishlistStatus.valueOf(type.trim().toUpperCase());
    }
}
