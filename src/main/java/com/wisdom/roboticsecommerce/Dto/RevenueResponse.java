package com.wisdom.roboticsecommerce.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RevenueResponse {
    private String unitName;
    private Integer unitValue;
    private Double revenue;
}
