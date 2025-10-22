package com.example.Service.Image;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ImageService {
    private static final String UPLOAD_DIR = "D:/LapTrinhWeb-10-2025/lap-trinh-web/src/main/webapp/image-upload";

    public String upload(HttpServletRequest request, String fieldName) throws IOException, ServletException {
        Part filePart = request.getPart(fieldName);
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String extension = "";

        int dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
            originalFileName = originalFileName.substring(0, dotIndex);
        }

        // Add current timestamp to filename
        String timestamp = String.valueOf(System.currentTimeMillis());
        String newFileName = originalFileName + "_" + timestamp + extension;

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        filePart.write(UPLOAD_DIR + File.separator + newFileName);

        return "image-upload/" + newFileName;
    }
}
