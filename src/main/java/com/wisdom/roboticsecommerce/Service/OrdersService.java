package com.wisdom.roboticsecommerce.Service;

import com.wisdom.roboticsecommerce.Dto.OrderRequest;
import com.wisdom.roboticsecommerce.Entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface OrdersService {
    Page<Map<String,Object>> search(Integer status, Integer pageNo, Integer pageSize, String sortBy, String sortType);
    Order updateStatus(Long id, Integer status);
    Order findById(Long id);
    Order createOrder(OrderRequest orderRequest);
    Page<Order> getAllOrdersByUserId(Pageable pageable);
    Page<Order> getOrdersByUserIdAndStatus(Integer status, Pageable pageable);
    void cancelOrder(Long orderId);
    void updateOrderStatus(Long orderId, Integer status);

    void addFeedback(Long orderId, Long productId, String content, Integer rating);

    Order detail(Long orderId);
}
