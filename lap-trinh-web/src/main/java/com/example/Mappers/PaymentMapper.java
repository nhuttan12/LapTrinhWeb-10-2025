package com.example.Mappers;


import com.example.DTO.PaymentDTO.PaymentDTO;
import com.example.Model.Payment;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(target = "method", expression = "java(payment.getMethod().getMethod())")
    @Mapping(target = "status", expression = "java(payment.getStatus().getStatus())")
    @Mapping(target = "paidAt", expression = "java(formatTimestamp(payment.getPaidAt()))")
    @Mapping(target = "createdAt", expression = "java(formatTimestamp(payment.getCreatedAt()))")
    PaymentDTO toDTO(Payment payment);

    default String formatTimestamp(Timestamp ts) {
        if (ts == null) return null;
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ts);
    }
}


