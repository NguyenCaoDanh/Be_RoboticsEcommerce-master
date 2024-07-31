package com.wisdom.roboticsecommerce.Service;

import com.wisdom.roboticsecommerce.Entities.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderDetailService {
    OrderDetail getOrderDetails(Long orderId);
    Page<OrderDetail> getAll(Long orderId, Pageable pageable);
}
