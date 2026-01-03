package com.example.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MergeDescription {
    private static final Set<String> RESERVED_FIELDS = Set.of(
            "id", "name", "price", "discount", "status",
            "os", "ram", "storage", "batteryCapacity",
            "screenSize", "screenResolution", "mobileNetwork",
            "cpu", "gpu", "waterResistance", "maxChargeWatt",
            "design", "memoryCard", "cpuSpeed", "releaseDate",
            "rating", "brandId", "brandName", "thumbnail", "action"
    );

    public String mergeDescription(Map<String, String> descriptionMap) {
        if (descriptionMap == null || descriptionMap.isEmpty()) {
            return null;
        }

        return descriptionMap.entrySet()
                .stream()
                .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("; "));
    }

    public Map<String, String> extractDescriptionMap(HttpServletRequest req) {
        Map<String, String> map = new LinkedHashMap<>();

        req.getParameterMap().forEach((key, values) -> {
            if (isDescriptionField(key)) {
                map.put(key, values[0]);
            }
        });

        return map;
    }

    private boolean isDescriptionField(String key) {
        return !RESERVED_FIELDS.contains(key);
    }

    /**
     * Extract dynamic specs fields from request (vi_terminology[], en_terminology[], value[])
     * and merge them into a single string: [en]:[vi]:[value]; ...
     * Also merges with existing descriptionMap.
     */
    public String mergeDynamicSpecs(HttpServletRequest req) {
        String[] viNames = req.getParameterValues("vi_terminology[]");
        String[] enNames = req.getParameterValues("en_terminology[]");
        String[] values  = req.getParameterValues("value[]");

        StringBuilder sb = new StringBuilder();

        // Merge dynamic fields first
        if (viNames != null && enNames != null && values != null) {
            for (int i = 0; i < values.length; i++) {
                String vi = viNames[i].trim();
                String en = enNames[i].trim();
                String val = values[i].trim();

                if (!vi.isEmpty() && !en.isEmpty() && !val.isEmpty()) {
                    if (sb.length() > 0) sb.append("; ");
                    sb.append(en).append(":").append(vi).append(":").append(val);
                }
            }
        }

        // Merge normal description fields
        Map<String, String> normalMap = extractDescriptionMap(req);
        for (Map.Entry<String, String> entry : normalMap.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if (val != null && !val.isEmpty()) {
                if (sb.length() > 0) sb.append("; ");
                sb.append(key).append(":").append(val);
            }
        }

        return sb.length() > 0 ? sb.toString() : null;
    }
}