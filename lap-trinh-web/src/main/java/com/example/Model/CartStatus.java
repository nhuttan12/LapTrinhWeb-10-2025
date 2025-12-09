package com.example.Model;

public enum CartStatus {
    OPEN, CHECKED_OUT, ABANDONED;
    // trả về giá trị lowercase để lưu vào DB
    public String getStatus() {
        return name().toLowerCase();
    }

    // chuyển từ String thành enum (case-insensitive)
    public static CartStatus fromString(String status) {
        for (CartStatus cs : CartStatus.values()) {
            if (cs.name().equalsIgnoreCase(status.trim())) {
                return cs;
            }
        }
        throw new IllegalArgumentException("Unknown CartStatus: " + status);
    }
}
