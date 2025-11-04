package com.example.DAO;

import com.example.DTO.Common.PagingMetaData;
import com.example.DTO.Common.PagingResponse;
import com.example.DTO.Common.SortDirection;
import com.example.Helper.BuildOrderPaging;
import com.example.Model.*;
import com.example.Service.Database.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductDAO {
    private final BuildOrderPaging buildOrderPaging;

    public ProductDAO() {
        this.buildOrderPaging = new BuildOrderPaging();
    }

    public PagingResponse<Product> getProductsPaging(
            int offset,
            int currentPage,
            int pageSize,
            List<String> sortBy,
            List<SortDirection> sortDirections
    ) throws SQLException {
        String sql = """
                    SELECT 
                        p.id as p_id, p.name, p.price, p.discount, p.status as p_status, p.category, 
                        p.created_at as p_created_at, p.updated_at as p_updated_at,
                        pi.id as pi_id, pi.image_id, pi.type as pi_type, pi.created_at as pi_created_at, pi.updated_at as pi_updated_at,
                        i.id as i_id, i.url as i_url, i.status as i_status, i.created_at as i_created_at, i.updated_at as i_updated_at
                    FROM products p
                    LEFT JOIN product_images pi ON pi.product_id = p.id AND pi.type = ?
                    LEFT JOIN images i ON i.id = pi.image_id
                    WHERE p.status = ?
                    %s
                    OFFSET ? LIMIT ?
                """;

        List<Product> products = new ArrayList<>();

        try (Connection conn = JDBCConnection.getConnection()) {

            // Build dynamic ORDER BY clause from meta
            String orderByClause = buildOrderPaging.buildOrderByClause("p", sortBy, sortDirections);
            sql = String.format(sql, orderByClause);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, ImageType.THUMBNAIL.getImageType());
                stmt.setString(2, ProductStatus.ACTIVE.getProductStatus());
                stmt.setInt(3, offset);
                stmt.setInt(4, pageSize);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        // Map Image
                        Image image = null;
                        if (rs.getInt("i_id") != 0) {
                            image = Image.builder()
                                    .id(rs.getInt("i_id"))
                                    .url(rs.getString("i_url"))
                                    .status(ImageStatus.fromString(rs.getString("i_status")))
                                    .createdAt(rs.getTimestamp("i_created_at"))
                                    .updatedAt(rs.getTimestamp("i_updated_at"))
                                    .build();
                        }

                        // Map ProductImage
                        ProductImage productImage = null;
                        if (rs.getInt("pi_id") != 0) {
                            productImage = ProductImage.builder()
                                    .id(rs.getInt("pi_id"))
                                    .imageId(rs.getInt("image_id"))
                                    .productId(rs.getInt("p_id"))
                                    .type(ImageType.fromString(rs.getString("pi_type")))
                                    .createdAt(rs.getTimestamp("pi_created_at"))
                                    .updatedAt(rs.getTimestamp("pi_updated_at"))
                                    .image(image)
                                    .build();
                        }

                        // Map Product
                        Product product = Product.builder()
                                .id(rs.getInt("p_id"))
                                .name(rs.getString("name"))
                                .price(rs.getDouble("price"))
                                .discount(rs.getDouble("discount"))
                                .status(ProductStatus.fromString(rs.getString("p_status")))
                                .category(rs.getString("category"))
                                .createdAt(rs.getTimestamp("p_created_at"))
                                .updatedAt(rs.getTimestamp("p_updated_at"))
                                .productImage(productImage)
                                .build();

                        products.add(product);
                    }
                }
            }

            // Count total items for meta info
            String countSql = """
                        SELECT COUNT(*) AS total 
                        FROM products p
                    WHERE p.status = ?
                    """;

            int totalItems = 0;
            try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
                countStmt.setString(1, ProductStatus.ACTIVE.getProductStatus());

                try (ResultSet countRs = countStmt.executeQuery()) {
                    if (countRs.next()) {
                        totalItems = countRs.getInt("total");
                    }
                }

                // Calculate total pages and navigation flags
                int totalPages = (int) Math.ceil((double) totalItems / pageSize);
                boolean hasNext = currentPage < totalPages;
                boolean hasPrevious = currentPage > 1;

                PagingMetaData updatedMeta = PagingMetaData.builder()
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .totalItems(totalItems)
                        .totalPages(totalPages)
                        .hasNext(hasNext)
                        .hasPrevious(hasPrevious)
                        .sortBy(sortBy)
                        .sortDirections(sortDirections)
                        .build();

                return PagingResponse.<Product>builder()
                        .items(products)
                        .meta(updatedMeta)
                        .build();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public PagingResponse<Product> getNewProductsPaging(
            int offset,
            int currentPage,
            int pageSize,
            List<String> sortBy,
            List<SortDirection> sortDirections
    ) throws SQLException {
        List<Product> products = new ArrayList<>();

        String sql = """
                    SELECT 
                        p.id as p_id, p.name, p.price, p.discount, p.status as p_status, p.category, 
                        p.created_at as p_created_at, p.updated_at as p_updated_at,
                        pi.id as pi_id, pi.image_id, pi.type as pi_type, pi.created_at as pi_created_at, pi.updated_at as pi_updated_at,
                        i.id as i_id, i.url as i_url, i.status as i_status, i.created_at as i_created_at, i.updated_at as i_updated_at
                    FROM products p
                    LEFT JOIN product_images pi ON pi.product_id = p.id AND pi.type = ?
                    LEFT JOIN images i ON i.id = pi.image_id
                    WHERE p.status = ?
                    ORDER BY p.created_at DESC
                    OFFSET ? LIMIT ?
                """;

        try (Connection conn = JDBCConnection.getConnection()) {

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, ImageType.THUMBNAIL.getImageType());
                stmt.setString(2, ProductStatus.ACTIVE.getProductStatus());
                stmt.setInt(3, offset);
                stmt.setInt(4, pageSize);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        // Map Image
                        Image image = null;
                        if (rs.getInt("i_id") != 0) {
                            image = Image.builder()
                                    .id(rs.getInt("i_id"))
                                    .url(rs.getString("i_url"))
                                    .status(ImageStatus.fromString(rs.getString("i_status")))
                                    .createdAt(rs.getTimestamp("i_created_at"))
                                    .updatedAt(rs.getTimestamp("i_updated_at"))
                                    .build();
                        }

                        // Map ProductImage
                        ProductImage productImage = null;
                        if (rs.getInt("pi_id") != 0) {
                            productImage = ProductImage.builder()
                                    .id(rs.getInt("pi_id"))
                                    .imageId(rs.getInt("image_id"))
                                    .productId(rs.getInt("p_id"))
                                    .type(ImageType.fromString(rs.getString("pi_type")))
                                    .createdAt(rs.getTimestamp("pi_created_at"))
                                    .updatedAt(rs.getTimestamp("pi_updated_at"))
                                    .image(image)
                                    .build();
                        }

                        // Map Product
                        Product product = Product.builder()
                                .id(rs.getInt("p_id"))
                                .name(rs.getString("name"))
                                .price(rs.getDouble("price"))
                                .discount(rs.getDouble("discount"))
                                .status(ProductStatus.fromString(rs.getString("p_status")))
                                .category(rs.getString("category"))
                                .createdAt(rs.getTimestamp("p_created_at"))
                                .updatedAt(rs.getTimestamp("p_updated_at"))
                                .productImage(productImage)
                                .build();

                        products.add(product);
                    }
                }

                // Count total items for meta info
                String countSql = """
                            SELECT COUNT(*) AS total 
                            FROM products p
                        WHERE p.status = ?
                        """;
                int totalItems = 0;
                try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
                    countStmt.setString(1, ProductStatus.ACTIVE.getProductStatus());

                    try (ResultSet countRs = countStmt.executeQuery()) {
                        if (countRs.next()) {
                            totalItems = countRs.getInt("total");
                        }
                    }

                    // Calculate total pages and navigation flags
                    int totalPages = (int) Math.ceil((double) totalItems / pageSize);
                    boolean hasNext = currentPage < totalPages;
                    boolean hasPrevious = currentPage > 1;

                    PagingMetaData updatedMeta = PagingMetaData.builder()
                            .currentPage(currentPage)
                            .pageSize(pageSize)
                            .totalItems(totalItems)
                            .totalPages(totalPages)
                            .hasNext(hasNext)
                            .hasPrevious(hasPrevious)
                            .sortBy(sortBy)
                            .sortDirections(sortDirections)
                            .build();

                    return PagingResponse.<Product>builder()
                            .items(products)
                            .meta(updatedMeta)
                            .build();

                }
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public PagingResponse<Product> getProductsPagingByBrandId(
            int brandId,
            int offset,
            int currentPage,
            int pageSize,
            List<String> sortBy,
            List<SortDirection> sortDirections
    ) throws SQLException {
        String countSql = """
                    SELECT COUNT(*) AS total
                    FROM products p
                    JOIN product_details pd ON p.id = pd.product_id
                    WHERE pd.brand_id = ?
                      AND p.status = ?
                """;

        String baseSql = """
                    SELECT 
                        p.id AS product_id,
                        p.name AS product_name,
                        p.price AS product_price,
                        p.discount AS product_discount,
                        p.status AS product_status,
                        p.category AS product_category,
                        p.created_at AS product_created_at,
                        p.updated_at AS product_updated_at,
                
                        pd.id AS detail_id,
                        pd.os, pd.ram, pd.storage, pd.battery_capacity,
                        pd.screen_size, pd.screen_resolution, pd.mobile_network,
                        pd.cpu, pd.gpu, pd.water_resistance, pd.max_charge_watt,
                        pd.design, pd.memory_card, pd.cpu_speed, pd.release_date,
                        pd.rating,
                        pd.created_at AS detail_created_at,
                        pd.updated_at AS detail_updated_at,
                
                        b.id AS brand_id, b.name AS brand_name, b.status AS brand_status,
                        b.created_at AS brand_created_at, b.updated_at AS brand_updated_at,
                
                        pi.id AS product_image_id, pi.image_id AS image_ref_id, pi.type AS image_type,
                        i.id AS image_id, i.url AS image_url, i.status AS image_status,
                        i.created_at AS image_created_at, i.updated_at AS image_updated_at
                
                    FROM products p
                    JOIN product_details pd ON p.id = pd.product_id
                    JOIN brands b ON pd.brand_id = b.id
                    LEFT JOIN product_images pi ON p.id = pi.product_id AND pi.type = 'thumbnail'
                    LEFT JOIN images i ON pi.image_id = i.id
                    WHERE pd.brand_id = ? 
                      AND p.status = ?
                """;
        // Add dynamic ORDER BY
        String orderClause = buildOrderPaging.buildOrderByClause("p", sortBy, sortDirections);
        baseSql += " " + orderClause + " LIMIT ? OFFSET ?";

        List<Product> products = new ArrayList<>();
        PagingMetaData meta;

        try (Connection conn = JDBCConnection.getConnection()) {

            int totalItems = 0;
            try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
                countStmt.setInt(1, brandId);
                countStmt.setString(2, ProductStatus.ACTIVE.getProductStatus());

                try (ResultSet rs = countStmt.executeQuery()) {
                    if (rs.next()) {
                        totalItems = rs.getInt("total");
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(baseSql)) {
                stmt.setInt(1, brandId);
                stmt.setString(2, ProductStatus.ACTIVE.getProductStatus());
                stmt.setInt(3, pageSize);
                stmt.setInt(4, offset);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Brand brand = Brand.builder()
                                .id(rs.getInt("brand_id"))
                                .name(rs.getString("brand_name"))
                                .status(BrandStatus.valueOf(rs.getString("brand_status").toUpperCase()))
                                .createdAt(rs.getTimestamp("brand_created_at"))
                                .updatedAt(rs.getTimestamp("brand_updated_at"))
                                .build();

                        ProductDetail detail = ProductDetail.builder()
                                .id(rs.getInt("detail_id"))
                                .productId(rs.getInt("product_id"))
                                .os(rs.getString("os"))
                                .ram(rs.getString("ram"))
                                .storage(rs.getString("storage"))
                                .batteryCapacity(rs.getInt("battery_capacity"))
                                .screenSize(rs.getDouble("screen_size"))
                                .screenResolution(rs.getString("screen_resolution"))
                                .mobileNetwork(rs.getString("mobile_network"))
                                .cpu(rs.getString("cpu"))
                                .gpu(rs.getString("gpu"))
                                .waterResistance(rs.getString("water_resistance"))
                                .maxChargeWatt(rs.getInt("max_charge_watt"))
                                .design(rs.getString("design"))
                                .memoryCard(rs.getString("memory_card"))
                                .cpuSpeed(rs.getDouble("cpu_speed"))
                                .releaseDate(rs.getTimestamp("release_date"))
                                .rating(rs.getDouble("rating"))
                                .createdAt(rs.getTimestamp("detail_created_at"))
                                .updatedAt(rs.getTimestamp("detail_updated_at"))
                                .brand(brand)
                                .build();

                        Image image = null;
                        if (rs.getInt("image_id") != 0) {
                            image = Image.builder()
                                    .id(rs.getInt("image_id"))
                                    .url(rs.getString("image_url"))
                                    .status(ImageStatus.valueOf(rs.getString("image_status").toUpperCase()))
                                    .createdAt(rs.getTimestamp("image_created_at"))
                                    .updatedAt(rs.getTimestamp("image_updated_at"))
                                    .build();
                        }

                        ProductImage productImage = ProductImage.builder()
                                .id(rs.getInt("product_image_id"))
                                .imageId(rs.getInt("image_ref_id"))
                                .productId(rs.getInt("product_id"))
                                .type(ImageType.valueOf(rs.getString("image_type").toUpperCase()))
                                .createdAt(rs.getTimestamp("product_created_at"))
                                .updatedAt(rs.getTimestamp("product_updated_at"))
                                .image(image)
                                .build();

                        Product product = Product.builder()
                                .id(rs.getInt("product_id"))
                                .name(rs.getString("product_name"))
                                .price(rs.getDouble("product_price"))
                                .discount(rs.getDouble("product_discount"))
                                .status(ProductStatus.valueOf(rs.getString("product_status").toUpperCase()))
                                .category(rs.getString("product_category"))
                                .createdAt(rs.getTimestamp("product_created_at"))
                                .updatedAt(rs.getTimestamp("product_updated_at"))
                                .productDetail(detail)
                                .productImage(productImage)
                                .build();

                        products.add(product);
                    }
                }
            }

            // Calculate total pages and navigation flags
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            boolean hasNext = currentPage < totalPages;
            boolean hasPrevious = currentPage > 1;

            // Build pagination metadata
            meta = PagingMetaData.builder()
                    .currentPage(currentPage)
                    .pageSize(pageSize)
                    .totalItems(totalItems)
                    .totalPages(totalPages)
                    .hasNext(hasNext)
                    .hasPrevious(hasPrevious)
                    .sortBy(sortBy)
                    .sortDirections(sortDirections)
                    .build();


            return PagingResponse.<Product>builder()
                    .items(products)
                    .meta(meta)
                    .build();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Product getProductByProductId(int productId) throws SQLException {
        String sql = """
                    SELECT id, name, price, discount, status, category, created_at, updated_at
                    FROM products
                    WHERE id = ?
                """;

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Product.builder()
                            .id(rs.getInt("id"))
                            .name(rs.getString("name"))
                            .price(rs.getDouble("price"))
                            .discount(rs.getDouble("discount"))
                            .status(ProductStatus.fromString(rs.getString("status")))
                            .category(rs.getString("category"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .build();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return null;
    }

    public Product getProductDetailByProductId(int productId) throws SQLException {
        String sql = """
                    SELECT
                        p.id AS product_id,
                        p.name AS product_name,
                        p.price,
                        p.discount,
                        p.status AS product_status,
                        p.category,
                        p.created_at AS product_created_at,
                        p.updated_at AS product_updated_at,
                
                        pd.id AS product_detail_id,
                        pd.os,
                        pd.ram,
                        pd.storage,
                        pd.battery_capacity,
                        pd.screen_size,
                        pd.screen_resolution,
                        pd.mobile_network,
                        pd.cpu,
                        pd.gpu,
                        pd.water_resistance,
                        pd.max_charge_watt,
                        pd.design,
                        pd.memory_card,
                        pd.cpu_speed,
                        pd.release_date,
                        pd.rating,
                        pd.description,
                        pd.created_at AS product_detail_created_at,
                        pd.updated_at AS product_detail_updated_at,
                
                        b.id AS brand_id,
                        b.name AS brand_name,
                        b.status AS brand_status,
                        b.created_at AS brand_created_at,
                        b.updated_at AS brand_updated_at,
                
                        pi.id AS product_image_id,
                        pi.image_id AS image_id,
                        pi.type AS image_type,
                        pi.created_at AS product_image_created_at,
                        pi.updated_at AS product_image_updated_at,
                
                        i.url AS image_url,
                        i.status AS image_status,
                        i.created_at AS image_created_at,
                        i.updated_at AS image_updated_at
                
                    FROM products p
                    LEFT JOIN product_details pd ON pd.product_id = p.id
                    LEFT JOIN brands b ON b.id = pd.brand_id
                    LEFT JOIN product_images pi ON pi.product_id = p.id AND pi.type = 'thumbnail'
                    LEFT JOIN images i ON i.id = pi.image_id
                    WHERE p.id = ?;
                """;

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return null;

                // --- Build Brand ---
                Brand brand = null;
                if (rs.getInt("brand_id") != 0) {
                    brand = Brand.builder()
                            .id(rs.getInt("brand_id"))
                            .name(rs.getString("brand_name"))
                            .status(BrandStatus.fromString(rs.getString("brand_status")))
                            .createdAt(rs.getTimestamp("brand_created_at"))
                            .updatedAt(rs.getTimestamp("brand_updated_at"))
                            .build();
                }

                // --- Build Image ---
                Image image = null;
                if (rs.getInt("image_id") != 0 && rs.getString("image_url") != null) {
                    image = Image.builder()
                            .id(rs.getInt("image_id"))
                            .url(rs.getString("image_url"))
                            .status(ImageStatus.fromString(rs.getString("image_status")))
                            .createdAt(rs.getTimestamp("image_created_at"))
                            .updatedAt(rs.getTimestamp("image_updated_at"))
                            .build();
                }

                // --- Build ProductImage ---
                ProductImage productImage = null;
                if (rs.getInt("product_image_id") != 0) {
                    productImage = ProductImage.builder()
                            .id(rs.getInt("product_image_id"))
                            .imageId(rs.getInt("image_id"))
                            .productId(rs.getInt("product_id"))
                            .type(ImageType.fromString(rs.getString("image_type")))
                            .createdAt(rs.getTimestamp("product_image_created_at"))
                            .updatedAt(rs.getTimestamp("product_image_updated_at"))
                            .image(image)
                            .build();
                }

                // --- Build ProductDetail ---
                ProductDetail productDetail = null;
                if (rs.getInt("product_detail_id") != 0) {
                    productDetail = ProductDetail.builder()
                            .id(rs.getInt("product_detail_id"))
                            .productId(rs.getInt("product_id"))
                            .os(rs.getString("os"))
                            .ram(rs.getString("ram"))
                            .storage(rs.getString("storage"))
                            .batteryCapacity((Integer) rs.getObject("battery_capacity"))
                            .screenSize((Double) rs.getObject("screen_size"))
                            .screenResolution(rs.getString("screen_resolution"))
                            .mobileNetwork(rs.getString("mobile_network"))
                            .cpu(rs.getString("cpu"))
                            .gpu(rs.getString("gpu"))
                            .waterResistance(rs.getString("water_resistance"))
                            .maxChargeWatt((Integer) rs.getObject("max_charge_watt"))
                            .design(rs.getString("design"))
                            .memoryCard(rs.getString("memory_card"))
                            .cpuSpeed((Double) rs.getObject("cpu_speed"))
                            .releaseDate(rs.getTimestamp("release_date"))
                            .rating((Double) rs.getObject("rating"))
                            .description(rs.getString("description"))
                            .createdAt(rs.getTimestamp("product_detail_created_at"))
                            .updatedAt(rs.getTimestamp("product_detail_updated_at"))
                            .brand(brand)
                            .build();
                }

                // --- Build Product ---
                return Product.builder()
                        .id(rs.getInt("product_id"))
                        .name(rs.getString("product_name"))
                        .price(rs.getDouble("price"))
                        .discount(rs.getDouble("discount"))
                        .status(ProductStatus.fromString(rs.getString("product_status")))
                        .category(rs.getString("category"))
                        .createdAt(rs.getTimestamp("product_created_at"))
                        .updatedAt(rs.getTimestamp("product_updated_at"))
                        .productDetail(productDetail)
                        .productImage(productImage)
                        .build();
            }
        }
    }

    public PagingResponse<Product> getProductsByFilterCriteriaPaging(
            int offset,
            int currentPage,
            int pageSize,
            List<String> sortBy,
            List<SortDirection> sortDirections,
            String[] osList,
            String[] ramList,
            String[] storageList,
            String[] chargeList
    ) {
        String countSql = """
                    SELECT COUNT(*) AS total
                    FROM products p
                    WHERE p.status = ?
                """;

        StringBuilder sql = new StringBuilder("""
                    SELECT p.*, pi.id AS pi_id, pi.image_id, pi.type, i.id AS img_id, i.url
                    FROM products p
                    LEFT JOIN product_images pi ON p.id = pi.product_id
                    LEFT JOIN images i ON pi.image_id = i.id
                    LEFT JOIN product_details pd ON p.id = pd.product_id
                    WHERE 1=1
                """);

        List<Object> params = new ArrayList<>();

        if (osList.length > 0) {
            sql.append(" AND (");
            for (int i = 0; i < osList.length; i++) {
                sql.append(" pd.os ILIKE ? ");
                params.add("%" + osList[i] + "%");
                if (i < osList.length - 1) sql.append(" OR ");
            }
            sql.append(")");
        }
        if (ramList.length > 0) {
            sql.append(" AND pd.ram = ANY(?)");
            params.add(ramList);
        }
        if (storageList.length > 0) {
            sql.append(" AND pd.storage = ANY(?)");
            params.add(storageList);
        }
        if (chargeList.length > 0) {
            sql.append(" AND pd.max_charge_watt = ANY(?)");
            params.add(chargeList);
        }

        if (sortBy != null && !sortBy.isEmpty()) {
            sql.append(" ORDER BY ");
            for (int i = 0; i < sortBy.size(); i++) {
                sql.append(sortBy.get(i)).append(" ");
                if (sortDirections != null && sortDirections.size() > i) {
                    sql.append(sortDirections.get(i).name());
                } else {
                    sql.append("ASC");
                }
                if (i < sortBy.size() - 1) sql.append(", ");
            }
        } else {
            sql.append(" ORDER BY p.created_at DESC");
        }

        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add(offset);

        List<Product> products = new ArrayList<>();

        try (Connection conn = JDBCConnection.getConnection()) {

            try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Product product = Product.builder()
                            .id(rs.getInt("id"))
                            .name(rs.getString("name"))
                            .price(rs.getDouble("price"))
                            .discount(rs.getDouble("discount"))
                            .status(ProductStatus.valueOf(rs.getString("status").toUpperCase()))
                            .category(rs.getString("category"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .productImage(ProductImage.builder()
                                    .id(rs.getInt("pi_id"))
                                    .imageId(rs.getInt("image_id"))
                                    .type(ImageType.valueOf(rs.getString("type").toUpperCase()))
                                    .image(Image.builder()
                                            .id(rs.getInt("img_id"))
                                            .url(rs.getString("url"))
                                            .build())
                                    .build())
                            .build();

                    products.add(product);
                }
            }

            int totalItems = 0;
            try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
                countStmt.setString(1, ProductStatus.ACTIVE.getProductStatus());

                try (ResultSet rs = countStmt.executeQuery()) {
                    if (rs.next()) {
                        totalItems = rs.getInt("total");
                    }
                }
            }

            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            boolean hasNext = currentPage < totalPages;
            boolean hasPrevious = currentPage > 1;

            PagingMetaData meta = PagingMetaData.builder()
                    .currentPage(currentPage)
                    .pageSize(pageSize)
                    .totalItems(totalItems)
                    .totalPages(totalPages)
                    .hasNext(hasNext)
                    .hasPrevious(hasPrevious)
                    .sortBy(sortBy)
                    .sortDirections(sortDirections)
                    .build();

            return PagingResponse.<Product>builder()
                    .items(products)
                    .meta(meta)
                    .build();

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching products by filter", e);
        }
    }
}
