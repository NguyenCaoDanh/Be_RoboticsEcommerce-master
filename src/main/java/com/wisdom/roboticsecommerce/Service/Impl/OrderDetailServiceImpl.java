package com.wisdom.roboticsecommerce.Service.Impl;

import com.wisdom.roboticsecommerce.Entities.OrderDetail;
import com.wisdom.roboticsecommerce.Mapper.Mapper;
import com.wisdom.roboticsecommerce.Repositories.OrderDetailRepository;
import com.wisdom.roboticsecommerce.Service.OrderDetailService;
import com.wisdom.roboticsecommerce.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private Mapper mapper;
    @Override
    public OrderDetail getOrderDetails(Long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.getOneCustom(
                orderDetailId,
                mapper.getUserIdByToken(),
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE);
        return orderDetail;
    }

    @Override
    public Page<OrderDetail> getAll(Long orderId, Pageable pageable) {
        Page<OrderDetail> orderDetailList = orderDetailRepository.getAllCustom(
                orderId,
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE,
                mapper.getUserIdByToken(),
                pageable
        );
        return orderDetailList;
    }
}
