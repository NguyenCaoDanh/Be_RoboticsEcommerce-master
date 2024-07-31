package com.wisdom.roboticsecommerce.Dto;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
public class CartRequest {
    private Long id;
    private Long productId;
    private Integer quantity;
}
