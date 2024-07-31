package com.wisdom.roboticsecommerce.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "product_image")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "image")
    private String image;
    @Column(name = "product_id")
    private Long productId;
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
