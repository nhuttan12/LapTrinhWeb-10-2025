package com.example.Model;

import java.sql.Timestamp;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    private Integer id;
    private Integer orderId;
    private Double amount;
    private PaymentMethod method;
    private String transactionId;     // Mã giao dịch từ nhà cung cấp
    private String provider;          // Nhà cung cấp thanh toán
    private String rawResponse;       // Phản hồi gốc từ API thanh toán (JSON)
    private Timestamp paidAt;         // Thời gian thanh toán
    private PaymentStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
