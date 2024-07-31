package com.wisdom.roboticsecommerce.Dto;

import lombok.Data;
@Data
public class ProductDto{
    private String code;
    private String name;
    private Integer quantity;
    private Double price;
    private Long categoryId;
    private Long promotionId;
//    private Long accountId;
}