package com.wisdom.roboticsecommerce.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VNPayRefund {
    private String vnPaymentId;
    private String orderInfo;
    private LocalDate createBy;
}
