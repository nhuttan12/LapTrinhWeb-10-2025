package com.example.DTO.Common;

public enum SortDirection {
    ASC,
    DESC;

    public String getImageType() {
        return name().toLowerCase();
    }
}
