package com.wisdom.roboticsecommerce.Repositories;

import com.wisdom.roboticsecommerce.Entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    Optional<ProductImage> findByIdAndDeleted(Long id, Integer deleted);
    Optional<ProductImage> findOneByProductIdAndStatusAndDeleted(Long productId, Integer status, Integer deleted);
}