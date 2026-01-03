package com.example.Utils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyzeDescription {
    /**
     * Parse a description string with dynamic fields.
     * Format can be:
     *   - [en]:[vi]:[value]  (preferred)
     *   - [vi]:[value]       (fallback)
     *
     * Returns a map:
     *   key -> original key (use en if exists, else vi)
     *   value -> map with keys: label (display), value (actual value), vi (Vietnamese name), en (English name)
     */
    public Map<String, Map<String, Object>> analyzeDescription(String description) {
        if (description == null || description.isBlank()) {
            return new LinkedHashMap<>();
        }

        Map<String, Map<String, Object>> result = new LinkedHashMap<>();

        Arrays.stream(description.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(entry -> {
                    String[] parts = entry.split(":");
                    String en = null;
                    String vi = null;
                    String value = null;

                    if (parts.length == 3) {
                        // [en]:[vi]:[value]
                        en = parts[0].trim();
                        vi = parts[1].trim();
                        value = parts[2].trim();
                    } else if (parts.length == 2) {
                        // [vi]:[value]
                        vi = parts[0].trim();
                        value = parts[1].trim();
                    } else {
                        // fallback: unknown format, skip
                        return;
                    }

                    // use en as key if exists, otherwise vi
                    String key = en != null && !en.isEmpty() ? en : vi;

                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("label", vi != null ? vi : key);
                    map.put("value", value);
                    map.put("vi", vi);
                    map.put("en", en);

                    result.put(key, map);
                });

        return result;
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
