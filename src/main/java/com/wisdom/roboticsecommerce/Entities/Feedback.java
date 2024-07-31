package com.wisdom.roboticsecommerce.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "rating")
    private Integer rating;
    @Column(name = "content", length = 1000)
    private String content;
    @Column(name = "reply", length = 1000)
    private String reply;
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
    @Column(name = "order_id")
    private Long orderId;
}
