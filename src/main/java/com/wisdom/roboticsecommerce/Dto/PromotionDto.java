package com.wisdom.roboticsecommerce.Dto;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.wisdom.roboticsecommerce.Entities.Promotion}
 */
@Data
public class PromotionDto {
    private Date startTime;
    private Date endTime;
    private Double value;
}