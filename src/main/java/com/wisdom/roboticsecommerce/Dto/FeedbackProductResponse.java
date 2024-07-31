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
public class FeedbackProductResponse {
    private Integer rating;
    private String content;
    @Temporal(TemporalType.DATE)
    private Date creatAt;
    private String fullName;
}
