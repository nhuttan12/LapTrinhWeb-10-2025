package com.example.Utils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyzeDescription {
    public Map<String, String> analyzeDescription(String description) {
        Map<String, String> result = Arrays.stream(description.split(";"))
                .map(String::trim)
                .filter(s -> s.contains(":"))
                .map(s -> s.split(":", 2))
                .collect(Collectors.toMap(
                        arr -> capitalizeFirstLetter(arr[0].trim()),
                        arr -> arr[1].trim(),
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        return result;
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
