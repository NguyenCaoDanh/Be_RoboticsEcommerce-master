package com.wisdom.roboticsecommerce.Service.Impl;

import com.wisdom.roboticsecommerce.Dto.PromotionDto;
import com.wisdom.roboticsecommerce.Entities.Promotion;
import com.wisdom.roboticsecommerce.Mapper.Mapper;
import com.wisdom.roboticsecommerce.Repositories.PromotionRepository;
import com.wisdom.roboticsecommerce.Service.PromotionService;
import com.wisdom.roboticsecommerce.Utils.Constants;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private Mapper mapper;
    @Override
    public Promotion create(PromotionDto promotionDto) {
        try {
            Promotion promotion = new Promotion();
            BeanUtils.copyProperties(promotionDto,promotion);
            promotion.setCreatAt(new Date());
            promotion.setStatus(Constants.STATUS_ACTIVE);
            promotion.setDeleted(Constants.DONT_DELETE);
            promotion.setAccountId(mapper.getUserIdByToken());
            promotionRepository.save(promotion);
            return promotion;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Promotion update(Promotion promotion, Long id) {
        try{
            Optional<Promotion> promotion1 = promotionRepository.findByIdAndDeleted(id,Constants.DONT_DELETE);
            if (promotion1.isEmpty()){
                throw new MessageDescriptorFormatException("Không tìm thấy khuyến mãi");
            }
            Promotion promotionUpdate = promotion1.get();

            promotionUpdate.setStartTime(promotion.getStartTime());
            promotionUpdate.setEndTime(promotion.getEndTime());
            promotionUpdate.setValue(promotion.getValue());
            promotionUpdate.setUpdateAt(new Date());
            promotionUpdate.setStatus(promotion.getStatus());
            promotionUpdate.setAccountId(mapper.getUserIdByToken());
            promotionRepository.save(promotionUpdate);
            return promotionUpdate;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleted(Long id) {
        try{
            Optional<Promotion> promotion = promotionRepository.findByIdAndDeleted(id,Constants.DONT_DELETE);
            if (promotion.isEmpty()){
                throw new MessageDescriptorFormatException("Không tìm thấy khuyến mãi");
            }
            Promotion promotionUpdate = promotion.get();
            promotionUpdate.setDeleted(Constants.DELETED);
            promotionRepository.save(promotionUpdate);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Promotion findById(Long id) {
        try{
            Optional<Promotion> promotion = promotionRepository.findByIdAndDeleted(id, Constants.DONT_DELETE);
            if (promotion == null){
                throw new MessageDescriptorFormatException("Không tìm thấy thông tin khuyến mãi");
            }
            return promotion.get();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Promotion> search() {
        try {
            var search = promotionRepository.findAll();
            return search;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
