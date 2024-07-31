package com.wisdom.roboticsecommerce.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "full_name", length = 200)
    private String fullname;
    @Column(name = "phone", length = 50)
    private String phone;
    @Column(name = "province_id")
    private Long provinceId;
    @Column(name = "district_id")
    private Long districtId;
    @Column(name = "ward_id")
    private Long wardId;
    @Column(name = "address_detail")
    private Long addressDetail;
    @Column(name = "createAt")
    @Temporal(TemporalType.DATE)
    private Date creatAt;
    @Temporal(TemporalType.DATE)
    @Column(name = "updateAt")
    private Date updateAt;
    @Column(name = "status")
    private Integer status;
    @Column(name = "deleted")
    private Integer deleted;
    @Column(name = "account_id")
    private Long accountId;
    @Column(name = "update_by_userId")
    private Long updateByUser;
}
