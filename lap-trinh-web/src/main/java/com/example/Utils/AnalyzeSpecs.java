package com.example.Utils;

import com.example.DTO.Products.GetProductDetailResponseDTO;

import java.util.LinkedHashMap;
import java.util.Map;

public class AnalyzeSpecs {
    public Map<String, Object> analyzeSpecs(GetProductDetailResponseDTO detailResponseDTO) {
        Map<String, Object> specs = new LinkedHashMap<>();
        specs.put("Hệ điều hành", detailResponseDTO.getOs());
        specs.put("RAM", detailResponseDTO.getRam() + " GB");
        specs.put("Bộ nhớ", detailResponseDTO.getStorage() + " GB");
        specs.put("Dung lượng pin", detailResponseDTO.getBatteryCapacity() + " mAh");
        specs.put("Kích thước màn hình", detailResponseDTO.getScreenSize() + " inch");
        specs.put("Độ phân giải", detailResponseDTO.getScreenResolution());
        specs.put("Mạng di động", detailResponseDTO.getMobileNetwork());
        specs.put("CPU", detailResponseDTO.getCpu());
        specs.put("GPU", detailResponseDTO.getGpu());
        specs.put("Chống nước", detailResponseDTO.getWaterResistance());
        specs.put("Công suất sạc tối đa", detailResponseDTO.getMaxChargeWatt() + " W");
        specs.put("Thiết kế", detailResponseDTO.getDesign());
        specs.put("Thẻ nhớ", detailResponseDTO.getMemoryCard());
        specs.put("Tốc độ CPU", detailResponseDTO.getCpuSpeed() + " GHz");
        specs.put("Ngày ra mắt", detailResponseDTO.getReleaseDate());

        return specs;
    }
}
