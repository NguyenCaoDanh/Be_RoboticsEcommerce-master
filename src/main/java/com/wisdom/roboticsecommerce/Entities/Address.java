package com.wisdom.roboticsecommerce.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name", length = 200)
    private String fullname;
    @Column(name = "phone", length = 50)
    private String phone;
    @Column(name = "tag_name", length = 50)
    private String tag_name;
    @Column(name = "province_id")
    private Long provinceId;
    @Column(name = "district_id")
    private Long districtId;
    @Column(name = "ward_id")
    private Long wardId;
    @Column(name = "address_detail", length = 1000)
    private String address_detail;
    @Column(name = "account_id")
    private Long accountId;
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
}
