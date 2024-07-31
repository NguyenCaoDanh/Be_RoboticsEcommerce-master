package com.wisdom.roboticsecommerce.Repositories;

import com.wisdom.roboticsecommerce.Entities.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {
}