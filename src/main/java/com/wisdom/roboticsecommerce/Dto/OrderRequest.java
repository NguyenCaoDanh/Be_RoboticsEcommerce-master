package com.wisdom.roboticsecommerce.Dto;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderRequest {
    private Long id;
//    private Double totalPrice;
    private String fullname;
    private String phone;
    private Long provinceId;
    private Long districtId;
    private Long wardId;
    private Long addressDetail;
    private List<Long> cartIdList;
}
