package com.example.Model;

public enum CartDetailStatus {
//    ACTIVE, INACTIVE;
    ACTIVE, REMOVED;
    // trả về giá trị lowercase để lưu vào DB
    public String getStatus() {
        return name().toLowerCase();
    }

    // chuyển từ String thành enum (case-insensitive)
    public static CartDetailStatus fromString(String status) {
        for (CartDetailStatus cs : CartDetailStatus.values()) {
            if (cs.name().equalsIgnoreCase(status.trim())) {
                return cs;
            }
        }
        throw new IllegalArgumentException("Unknown CartDetailStatus: " + status);
    }
}
