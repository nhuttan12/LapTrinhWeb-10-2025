package com.example.Utils;

import com.example.DTO.Products.GetProductDetailResponseDTO;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnalyzeSpecs {
    public Map<String, Map<String, Object>> analyzeSpecs(GetProductDetailResponseDTO d) {

        Map<String, Map<String, Object>> specs = new LinkedHashMap<>();

        specs.put("os", spec("Hệ điều hành", d.getOs()));
        specs.put("ram", spec("RAM (GB)", d.getRam()));
        specs.put("storage", spec("Bộ nhớ (GB)", d.getStorage()));
        specs.put("batteryCapacity", spec("Dung lượng pin (mAh)", d.getBatteryCapacity()));
        specs.put("screenSize", spec("Kích thước màn hình (inch)", d.getScreenSize()));
        specs.put("screenResolution", spec("Độ phân giải", d.getScreenResolution()));
        specs.put("mobileNetwork", spec("Mạng di động", d.getMobileNetwork()));
        specs.put("cpu", spec("CPU", d.getCpu()));
        specs.put("gpu", spec("GPU", d.getGpu()));
        specs.put("waterResistance", spec("Chống nước", d.getWaterResistance()));
        specs.put("maxChargeWatt", spec("Công suất sạc tối đa (W)", d.getMaxChargeWatt()));
        specs.put("design", spec("Thiết kế", d.getDesign()));
        specs.put("memoryCard", spec("Thẻ nhớ", d.getMemoryCard()));
        specs.put("cpuSpeed", spec("Tốc độ CPU (GHz)", d.getCpuSpeed()));
        specs.put("releaseDate", spec("Ngày ra mắt", d.getReleaseDate()));

        return specs;
    }

    /** helper */
    private Map<String, Object> spec(String label, Object value) {
        Map<String, Object> m = new HashMap<>();
        m.put("label", label);
        m.put("value", value);
        return m;
    }
}
