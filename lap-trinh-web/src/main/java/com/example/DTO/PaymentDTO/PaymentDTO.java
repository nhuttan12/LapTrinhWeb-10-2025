package com.example.DTO.PaymentDTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Integer id;
    private Integer orderId;
    private Double amount;
    private String method;         // cod, momo_test...
    private String status;         // pending, completed...
    private String provider;
    private String transactionId;
    private String paidAt;         // dạng yyyy-MM-dd HH:mm:ss
    private String createdAt;
}
