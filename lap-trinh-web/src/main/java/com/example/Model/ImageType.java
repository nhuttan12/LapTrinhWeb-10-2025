package com.example.Model;

public enum ImageType {
    THUMBNAIL, GALLERY, PRODUCT;

    public String getImageType() {
        return name().toLowerCase();
    }

    public static ImageType fromString(String type) {
        return ImageType.valueOf(type.trim().toUpperCase());
    }
}
