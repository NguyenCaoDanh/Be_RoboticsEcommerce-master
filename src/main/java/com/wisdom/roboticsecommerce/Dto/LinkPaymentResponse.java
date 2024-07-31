package com.wisdom.roboticsecommerce.Dto;

import lombok.Data;

@Data
public class LinkPaymentResponse {
    private String status;
    private String message;
    private Long amount;
    private String url;
}
