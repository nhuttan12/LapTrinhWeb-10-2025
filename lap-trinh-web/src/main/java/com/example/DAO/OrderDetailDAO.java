package com.example.DAO;

import com.example.Model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {
    private final Connection conn;

    public OrderDetailDAO(Connection conn) {
        this.conn = conn;
    }

    public List<OrderDetail> getOrderDetailsPaging(int orderId, int userId, int limit, int offset) {
        List<OrderDetail> orderDetails = new ArrayList<>();

        String sql = """
                    SELECT 
                        od.id AS od_id,
                        od.quantity AS od_quantity,
                        od.price AS od_price,
                        od.created_at AS od_created_at,
                        od.updated_at AS od_updated_at,
                        p.id AS p_id,
                        p.name AS p_name,
                        p.price AS p_price,
                        p.discount AS p_discount,
                        p.status AS p_status,
                        p.category AS p_category,
                        p.created_at AS p_created_at,
                        p.updated_at AS p_updated_at,
                        pi.id AS pi_id,
                        pi.product_id AS pi_product_id,
                        pi.type AS pi_type,
                        i.id AS i_id,
                        i.url AS i_url,
                        i.status AS i_status,
                        i.created_at AS i_created_at,
                        i.updated_at AS i_updated_at
                    FROM orders o
                    JOIN order_details od ON o.id = od.order_id
                    JOIN products p ON od.product_id = p.id
                    LEFT JOIN product_images pi 
                        ON p.id = pi.product_id AND pi.type = ?
                    LEFT JOIN images i 
                        ON pi.image_id = i.id
                    WHERE o.id = ? AND o.user_id = ?
                    ORDER BY od.id
                    LIMIT ? OFFSET ?
                """;

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, ImageType.THUMBNAIL.getImageType());
                stmt.setInt(2, orderId);
                stmt.setInt(3, userId);
                stmt.setInt(4, limit);
                stmt.setInt(5, offset);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Image image = null;
                        if (rs.getString("i_url") != null) {
                            image = Image.builder()
                                    .id(rs.getInt("i_id"))
                                    .url(rs.getString("i_url"))
                                    .status(rs.getString("i_status") != null
                                            ? ImageStatus.fromString(rs.getString("i_status"))
                                            : null)
                                    .createdAt(rs.getTimestamp("i_created_at"))
                                    .updatedAt(rs.getTimestamp("i_updated_at"))
                                    .build();
                        }

                        ProductImage productImage = null;
                        if (rs.getString("pi_type") != null) {
                            productImage = ProductImage.builder()
                                    .id(rs.getInt("pi_id"))
                                    .productId(rs.getInt("pi_product_id"))
                                    .type(ImageType.fromString(rs.getString("pi_type")))
                                    .image(image)
                                    .build();
                        }

                        Product product = Product.builder()
                                .id(rs.getInt("p_id"))
                                .name(rs.getString("p_name"))
                                .price(rs.getDouble("p_price"))
                                .discount(rs.getDouble("p_discount"))
                                .status(ProductStatus.fromString(rs.getString("p_status")))
                                .category(rs.getString("p_category"))
                                .createdAt(rs.getTimestamp("p_created_at"))
                                .updatedAt(rs.getTimestamp("p_updated_at"))
                                .productImage(productImage)
                                .build();

                        OrderDetail orderDetail = OrderDetail.builder()
                                .id(rs.getInt("od_id"))
                                .orderId(orderId)
                                .productId(product.getId())
                                .quantity(rs.getInt("od_quantity"))
                                .price(rs.getDouble("od_price"))
                                .createdAt(rs.getTimestamp("od_created_at"))
                                .updatedAt(rs.getTimestamp("od_updated_at"))
                                .product(product)
                                .build();

                        orderDetails.add(orderDetail);
                    }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transaction rolled back due to error.");
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return orderDetails;
    }
}
