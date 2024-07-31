package com.wisdom.roboticsecommerce.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GHNCalculateFeeRequest {
    private Long serviceId;
    private String coupon;
    private Long toDistrictId;
    private String toWardCode;
    private Long weight;
    private Long width;
    private Long length;
    private Long height;
    private Long orderValue;
}
