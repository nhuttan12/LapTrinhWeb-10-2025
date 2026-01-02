package com.example.Controller.Admin;

import com.example.DTO.Products.CreateProductRequestDTO;
import com.example.DTO.Products.GetProductsPagingResponseDTO;
import com.example.DTO.Products.UpdateProductRequestDTO;
import com.example.Model.Product;
import com.example.Model.ProductDetail;
import com.example.Model.ProductStatus;
import com.example.Service.Admin.AdminProductService;
import com.example.Service.Database.JDBCConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@WebServlet("/admin/products/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB per file
        maxRequestSize = 1024 * 1024 * 50    // 50MB total
)
public class AdminProduct extends HttpServlet {

    private AdminProductService productService;

    @Override
    public void init() {
        try {
            Connection conn = JDBCConnection.getConnection();
            productService = new AdminProductService(new com.example.DAO.AdminProductDAO(conn));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            // LIST products
            int page = 1;
            int limit = 10;
            String pageParam = req.getParameter("page");
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException ignored) {}
            }

            try {
                List<GetProductsPagingResponseDTO> products = productService.getProductsPaging(page, limit);
                int totalPages = productService.getTotalPages(limit);

                req.setAttribute("products", products);
                req.setAttribute("currentPage", page);
                req.setAttribute("totalPages", totalPages);

                req.getRequestDispatcher("/admin/pages/productManagement/productManagement.jsp")
                        .forward(req, resp);
            } catch (SQLException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            }
        } else if (path.equals("/add")) {
            // SHOW add form
            req.getRequestDispatcher("/admin/pages/productManagement/product-form.jsp")
                    .forward(req, resp);
        } else if (path.equals("/edit")) {
            // SHOW edit form: load product + detail and set to request
            String idParam = req.getParameter("id");
            if (idParam != null) {
                try {
                    int productId = Integer.parseInt(idParam);
                    Product p = productService.getProductById(productId);
                    ProductDetail d = productService.getProductDetailByProductId(productId);

                    req.setAttribute("product", p);
                    req.setAttribute("detail", d);
                } catch (SQLException e) {
                    e.printStackTrace();
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
                    return;
                }
            }
            req.getRequestDispatcher("/admin/pages/productManagement/product-form.jsp")
                    .forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // ensure UTF-8 correctness
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        try {
            switch (action) {

                case "create" -> {
                    // handle file upload for thumbnail (optional)
                    String thumbnailUrl = handleThumbnailUpload(req);

                    CreateProductRequestDTO dto = buildCreateDTO(req, thumbnailUrl);
                    productService.createProduct(dto);
                    resp.sendRedirect(req.getContextPath() + "/admin/products?success=1");
                }

                case "update" -> {
                    Integer id = Integer.valueOf(req.getParameter("id"));

                    // load existing
                    Product existing = productService.getProductById(id);
                    ProductDetail detail = productService.getProductDetailByProductId(id);

                    // handle thumbnail upload: if uploaded, returns new path; else returns null
                    String thumbnailUrl = handleThumbnailUpload(req);
                    UpdateProductRequestDTO dto = buildUpdateDTO(req, thumbnailUrl);

                    productService.updateProduct(existing, detail, dto);
                    resp.sendRedirect(req.getContextPath() + "/admin/products?updated=1");
                }

                case "delete" -> {
                    Integer id = Integer.valueOf(req.getParameter("id"));
                    productService.softDelete(id);
                    resp.sendRedirect(req.getContextPath() + "/admin/products?deleted=1");
                }

                default -> resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500);
        }
    }

    /**
     * Nếu có file upload field name = "thumbnailFile" thì lưu và trả về URL (relative path)
     * Nếu không upload, trả về null
     */
    private String handleThumbnailUpload(HttpServletRequest req) {
        try {
            Part filePart = req.getPart("thumbnailFile");
            if (filePart != null && filePart.getSize() > 0) {
                String submitted = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String ext = "";
                int idx = submitted.lastIndexOf('.');
                if (idx > 0) ext = submitted.substring(idx);

                String newName = UUID.randomUUID().toString() + ext;

                // upload folder inside webapp (so static access via URL /uploads/products/<name>)
                String uploadDir = req.getServletContext().getRealPath("/uploads/products/");
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) uploadFolder.mkdirs();

                Path out = Paths.get(uploadDir, newName);

                try (InputStream in = filePart.getInputStream()) {
                    Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING);
                }

                // return relative url used in DB and UI
                return req.getContextPath() + "/uploads/products/" + newName;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // swallow - return null (we'll fallback to text URL if provided)
        }
        return null;
    }

    /* DTO builder for create: thumbnailUrl param (may be null) */
    private CreateProductRequestDTO buildCreateDTO(HttpServletRequest req, String uploadedThumbnailUrl) {
        String thumbnailInput = req.getParameter("thumbnail"); // text input
        String thumbnail = (uploadedThumbnailUrl != null && !uploadedThumbnailUrl.isBlank()) ? uploadedThumbnailUrl : thumbnailInput;

        return CreateProductRequestDTO.builder()
                .name(req.getParameter("name"))
                .price(parseDoubleSafe(req.getParameter("price")))
                .discount(parseDoubleSafe(req.getParameter("discount")))
                .status(ProductStatus.fromString(req.getParameter("status")))

                .os(req.getParameter("os"))
                .ram(parseIntSafe(req.getParameter("ram")))
                .storage(parseIntSafe(req.getParameter("storage")))
                .batteryCapacity(parseIntSafe(req.getParameter("batteryCapacity")))
                .screenSize(parseDoubleSafe(req.getParameter("screenSize")))
                .screenResolution(req.getParameter("screenResolution"))
                .mobileNetwork(req.getParameter("mobileNetwork"))
                .cpu(req.getParameter("cpu"))
                .gpu(req.getParameter("gpu"))
                .waterResistance(req.getParameter("waterResistance"))
                .maxChargeWatt(parseIntSafe(req.getParameter("maxChargeWatt")))
                .design(req.getParameter("design"))
                .memoryCard(req.getParameter("memoryCard"))
                .cpuSpeed(parseDoubleSafe(req.getParameter("cpuSpeed")))
                .releaseDate(parseTimestampSafe(req.getParameter("releaseDate")))
                .rating(parseDoubleSafe(req.getParameter("rating")))
                .description(req.getParameter("description"))

                .brandId(parseIntSafe(req.getParameter("brandId")))
                .thumbnail(thumbnail)
                .build();
    }

    /* DTO builder for update: thumbnailUrl param (may be null) */
    private UpdateProductRequestDTO buildUpdateDTO(HttpServletRequest req, String uploadedThumbnailUrl) {
        String thumbnailInput = req.getParameter("thumbnail"); // text input
        String thumbnail = (uploadedThumbnailUrl != null && !uploadedThumbnailUrl.isBlank()) ? uploadedThumbnailUrl : thumbnailInput;

        return UpdateProductRequestDTO.builder()
                .id(parseIntSafe(req.getParameter("id")))
                .name(req.getParameter("name"))
                .price(parseDoubleSafe(req.getParameter("price")))
                .discount(parseDoubleSafe(req.getParameter("discount")))
                .status(ProductStatus.fromString(req.getParameter("status")))

                .os(req.getParameter("os"))
                .ram(parseIntSafe(req.getParameter("ram")))
                .storage(parseIntSafe(req.getParameter("storage")))
                .batteryCapacity(parseIntSafe(req.getParameter("batteryCapacity")))
                .screenSize(parseDoubleSafe(req.getParameter("screenSize")))
                .screenResolution(req.getParameter("screenResolution"))
                .mobileNetwork(req.getParameter("mobileNetwork"))
                .cpu(req.getParameter("cpu"))
                .gpu(req.getParameter("gpu"))
                .waterResistance(req.getParameter("waterResistance"))
                .maxChargeWatt(parseIntSafe(req.getParameter("maxChargeWatt")))
                .design(req.getParameter("design"))
                .memoryCard(req.getParameter("memoryCard"))
                .cpuSpeed(parseDoubleSafe(req.getParameter("cpuSpeed")))
                .releaseDate(parseTimestampSafe(req.getParameter("releaseDate")))
                .rating(parseDoubleSafe(req.getParameter("rating")))
                .description(req.getParameter("description"))

                .brandId(parseIntSafe(req.getParameter("brandId")))
                .thumbnail(thumbnail)
                .build();
    }

    /* safe parsers */
    private Integer parseIntSafe(String s) {
        try { if (s == null || s.isBlank()) return null; return Integer.valueOf(s); } catch (Exception e) { return null; }
    }
    private Double parseDoubleSafe(String s) {
        try { if (s == null || s.isBlank()) return null; return Double.valueOf(s); } catch (Exception e) { return null; }
    }
    private Timestamp parseTimestampSafe(String s) {
        try { if (s == null || s.isBlank()) return null; return Timestamp.valueOf(s + " 00:00:00"); } catch (Exception e) { return null; }
    }
}
