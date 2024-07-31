package com.wisdom.roboticsecommerce.Service.Impl;

import com.wisdom.roboticsecommerce.Entities.ProductImage;
import com.wisdom.roboticsecommerce.Repositories.ProductImageRepository;
import com.wisdom.roboticsecommerce.Service.ProductImageService;
import com.wisdom.roboticsecommerce.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductImageServiceImpl implements ProductImageService {
    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    public String getImage(Long productId) {
        Optional<ProductImage> productImageOptional = productImageRepository.findOneByProductIdAndStatusAndDeleted(
                productId,
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE
        );
        if (productImageOptional.isEmpty()) {
            return null;
        } else {
            return productImageOptional.get().getImage();
        }
    }
}
