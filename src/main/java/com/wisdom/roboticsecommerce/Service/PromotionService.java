package com.wisdom.roboticsecommerce.Service;

import com.wisdom.roboticsecommerce.Dto.PromotionDto;
import com.wisdom.roboticsecommerce.Entities.Promotion;

import java.util.List;

public interface PromotionService {
    Promotion create(PromotionDto promotionDto);
    Promotion update(Promotion promotion, Long id);
    void deleted(Long id);
    Promotion findById(Long id);
    List<Promotion> search();
}
