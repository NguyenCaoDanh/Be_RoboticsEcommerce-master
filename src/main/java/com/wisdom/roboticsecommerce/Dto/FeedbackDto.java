package com.wisdom.roboticsecommerce.Dto;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {
    private Long id;
    private Long orderId;
    private Long productId;
    private Integer rating;
    private String content;
    private String reply;
    @Temporal(TemporalType.DATE)
    private Date creatAt;
    @Temporal(TemporalType.DATE)
    private Date updateAt;
    private Integer status;
    private Integer deleted;
    private Long accountId;
}
