package com.wisdom.roboticsecommerce.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "code", length =50)
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "price")
    private Double price;
    @Column(name = "description", length = 1000)
    private String description;
    @Column(name = "category_id")
    private Long categoryId;
    @Column(name = "promotion_id")
    private Long promotionId;
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
