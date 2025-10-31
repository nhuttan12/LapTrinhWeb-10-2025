package com.example.Model;

public enum ImageStatus {
    ACTIVE, INACTIVE;

    public String getImageStatus() {
        return name().toLowerCase();
    }

    public static ImageStatus fromString(String type) {
        return ImageStatus.valueOf(type.trim().toUpperCase());
    }
}
