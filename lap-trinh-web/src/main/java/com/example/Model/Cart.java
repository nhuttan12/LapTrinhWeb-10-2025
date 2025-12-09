package com.example.Model;

import java.sql.Timestamp;
import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    private Integer id;
    private Integer userId;
    private CartStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<CartDetail> cartDetails;

    public double getTotalPrice() {
        if (cartDetails == null || cartDetails.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        for (CartDetail detail : cartDetails) {
            if (detail.getProduct() != null && detail.getQuantity() != null) {
                Double price = detail.getProduct().getPrice(); // Giả sử Product có getPrice() trả về double
                if (price != null) {
                    total += price * detail.getQuantity();
                }
            }
        }
        return total;
    }
}
