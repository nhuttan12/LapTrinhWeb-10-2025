package com.example.Controller.Admin;

import com.example.DTO.Products.CreateProductRequestDTO;
import com.example.DTO.Products.GetProductDetailResponseDTO;
import com.example.DTO.Products.GetProductsPagingResponseDTO;
import com.example.DTO.Products.UpdateProductRequestDTO;
import com.example.Model.Brand;
import com.example.Model.Product;
import com.example.Model.ProductDetail;
import com.example.Model.ProductStatus;
import com.example.Service.Admin.AdminProductService;
import com.example.Service.Brands.BrandService;
import com.example.Service.Database.JDBCConnection;
import com.example.Service.Image.ImageService;
import com.example.Service.Product.ProductService;
import com.example.Utils.AnalyzeSpecs;
import com.example.Utils.MergeDescription;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet("/admin/products/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB per file
        maxRequestSize = 1024 * 1024 * 50    // 50MB total
)
public class AdminProduct extends HttpServlet {

    private AdminProductService adminProductService;
    private ProductService productService;
    private AnalyzeSpecs analyzeSpecs;
    private MergeDescription mergeDescription;
    private BrandService brandService;
    private ImageService imageService;

    @Override
    public void init() {
        try {
            Connection conn = JDBCConnection.getConnection();
            adminProductService = new AdminProductService(new com.example.DAO.AdminProductDAO(conn));
            analyzeSpecs = new AnalyzeSpecs();
            productService = new ProductService();
            mergeDescription = new MergeDescription();
            brandService = new BrandService();
            imageService = new ImageService();
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
                } catch (NumberFormatException ignored) {
                }
            }

            try {
                List<GetProductsPagingResponseDTO> products = adminProductService.getProductsPaging(page, limit);
                int totalPages = adminProductService.getTotalPages(limit);

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
            GetProductDetailResponseDTO empty = GetProductDetailResponseDTO.builder()
                    .status("active")
                    .build();

            Map<String, Map<String, Object>> specs = analyzeSpecs.analyzeSpecs(empty);

            req.setAttribute("detail", empty);
            req.setAttribute("specs", specs);

            req.getRequestDispatcher("/admin/pages/productManagement/product-form.jsp")
                    .forward(req, resp);
        } else if (path.equals("/edit")) {
            // SHOW edit form: load product + detail and set to request
            String idParam = req.getParameter("id");

            if (idParam != null) {
                int productId = Integer.parseInt(idParam);

                GetProductDetailResponseDTO detailResponseDTO = productService.getProductDetailByProductId(productId);
                System.out.println("Detail response: " + detailResponseDTO);

                Map<String, Map<String, Object>> specs = analyzeSpecs.analyzeSpecs(detailResponseDTO);
                System.out.println("Specs: " + specs);

                req.setAttribute("specs", specs);
                req.setAttribute("detail", detailResponseDTO);
            }

            req.getRequestDispatcher("/admin/pages/productManagement/product-form.jsp")
                    .forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // ensure UTF-8 correctness
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        System.out.println("Do post action: " + action);

        try {
            switch (action) {

                case "create" -> {
                    // handle file upload for thumbnail (optional)
                    String thumbnailUrl = imageService.upload(req, "thumbnailFile");

                    CreateProductRequestDTO dto = buildCreateDTO(req, thumbnailUrl);

                    adminProductService.createProduct(dto);
                    resp.sendRedirect(req.getContextPath() + "/admin/products?success=1");
                }

                case "update" -> {
                    Integer id = Integer.valueOf(req.getParameter("id"));

                    // load existing
                    Product existing = adminProductService.getProductById(id);
                    ProductDetail detail = adminProductService.getProductDetailByProductId(id);

                    // handle thumbnail upload: if uploaded, returns new path; else returns null
                    String thumbnailUrl = imageService.upload(req, "thumbnailFile");

                    if (thumbnailUrl == null) {
                        thumbnailUrl = existing.getThumbnail();
                    }

                    UpdateProductRequestDTO dto = buildUpdateDTO(req, thumbnailUrl);

                    adminProductService.updateProduct(existing, detail, dto);
                    resp.sendRedirect(req.getContextPath() + "/admin/products/edit?id=" + id);
                }

                case "delete" -> {
                    Integer id = Integer.valueOf(req.getParameter("id"));
                    adminProductService.softDelete(id);
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
        /**
         * Get dynamic description
         */
        String mergedDescription = mergeDescription.mergeDynamicSpecs(req);

        /**
         * Get brand by name
         */
        Brand brand = brandService.getBrandByName(req.getParameter("brandName"));

        /**
         * Create new brand if not exist
         */
        if (brand == null) {
            brand = brandService.createBrandByBrandName(req.getParameter("brandName"));
        }

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
                .description(mergedDescription)

                .brandId(brand.getId())
                .thumbnail(uploadedThumbnailUrl)
                .build();
    }

    /* DTO builder for update: thumbnailUrl param (may be null) */
    private UpdateProductRequestDTO buildUpdateDTO(HttpServletRequest req, String uploadedThumbnailUrl) {
        /**
         * Get description
         */
        Map<String, String> descriptionMap = mergeDescription.extractDescriptionMap(req);
        String mergedDescription = mergeDescription.mergeDescription(descriptionMap);

        /**
         * Get brand by name
         */
        Brand brand = brandService.getBrandByName(req.getParameter("brandName"));

        /**
         * Create new brand if not exist
         */
        if (brand == null) {
            brand = brandService.createBrandByBrandName(req.getParameter("brandName"));
        }

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
                .description(mergedDescription)
                .brandId(brand.getId())
                .thumbnail(uploadedThumbnailUrl)
                .build();
    }

    /* safe parsers */
    private Integer parseIntSafe(String s) {
        try {
            if (s == null || s.isBlank()) return null;
            return Integer.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    private Double parseDoubleSafe(String s) {
        try {
            if (s == null || s.isBlank()) return null;
            return Double.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    private Timestamp parseTimestampSafe(String s) {
        try {
            if (s == null || s.isBlank()) return null;
            return Timestamp.valueOf(s + " 00:00:00");
        } catch (Exception e) {
            return null;
        }
    }
}
