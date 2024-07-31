package com.wisdom.roboticsecommerce.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GHNOrderRequest {
    private Long orderId;
    private String note;
    private String requiredNote;
    private String toName;
    private String toPhone;
    private String toAddress;
    private String toWardCode;
    private Long toDistrictId;
    private Long codAmount;
    private String content;
    private Integer weight;
    private Integer length;
    private Integer width;
    private Integer height;
    private Long serviceId;
    private Long serviceTypeId;
}
