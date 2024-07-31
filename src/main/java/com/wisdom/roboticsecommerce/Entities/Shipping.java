package com.wisdom.roboticsecommerce.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "shipping")
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "shipping_order_code", length = 50)
    private String shippingCode;
    @Column(name = "total_free")
    private Double totalFree;
    @Column(name = "expected_delivery_time")
    @Temporal(TemporalType.DATE)
    private Date expected_delivery_time;
    @Column(name = "trans_type", length = 200)
    private String trans_type;
    @Column(name = "order_id")
    private Long orderId;
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
}
