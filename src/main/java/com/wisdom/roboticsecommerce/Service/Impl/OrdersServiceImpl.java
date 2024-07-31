package com.wisdom.roboticsecommerce.Service.Impl;

import com.wisdom.roboticsecommerce.Dto.OderDetailRequest;
import com.wisdom.roboticsecommerce.Dto.OrderRequest;
import com.wisdom.roboticsecommerce.Entities.*;
import com.wisdom.roboticsecommerce.Mapper.Mapper;
import com.wisdom.roboticsecommerce.Mapper.PageMapper;
import com.wisdom.roboticsecommerce.Repositories.*;
import com.wisdom.roboticsecommerce.Service.OrdersService;
import com.wisdom.roboticsecommerce.Utils.Constants;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private PageMapper pageMapper;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private CartRepository cartRepository;
    @Override
    public Page<Map<String, Object>> search(Integer status, Integer pageNo, Integer pageSize, String sortBy, String sortType) {
        try{
            var page = pageMapper.customPage(pageNo, pageSize, sortBy, sortType);
            var search  = orderRepository.search(status, page);
            return search;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Order updateStatus(Long id, Integer status) {
        try {
            Optional<Order> orderOptional = orderRepository.findByIdAndDeleted(id, Constants.DONT_DELETE);
            if (orderOptional.isEmpty()){
                throw new MessageDescriptorFormatException("Không tìm thấy đơn hàng");
            }
            Order order = orderOptional.get();
            order.setStatus(status);
            order.setUpdateAt(new Date());
            order.setUpdateByUser(mapper.getUserIdByToken());
            orderRepository.save(order);
            return order;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Order findById(Long id) {
        Order order = orderRepository.getOneCustom(
                id,
                mapper.getUserIdByToken(),
                Constants.DONT_DELETE
        );
        if (order == null) {
            throw new RuntimeException("Không tìm thấy đơn hàng");
        }

        return order;
    }

    @Override
    public Order createOrder(OrderRequest orderRequest) {
        Double totalPrice = 0.0;
        Order order = new Order();
        Long accountId = mapper.getUserIdByToken();
        order.setAccountId(accountId);
        order.setAddressDetail(orderRequest.getAddressDetail());
        order.setFullname(orderRequest.getFullname());
        order.setPhone(orderRequest.getPhone());
        order.setProvinceId(orderRequest.getProvinceId());
        order.setDistrictId(orderRequest.getDistrictId());
        order.setWardId(orderRequest.getWardId());
//        order.setTotalPrice(orderRequest.getTotalPrice());
        order.setTotalPrice(totalPrice);
        order.setStatus(Constants.STATUS_ACTIVE);
        order.setDeleted(Constants.DONT_DELETE);
        order.setCreatAt(new Date());
        order.setUpdateAt(new Date());

        orderRepository.save(order);
        for (Long item : orderRequest.getCartIdList()){
            // Kiểm tra sản phẩm có nằm trong giỏ hàng không
            Cart cart = cartRepository.getOneCustom(
                    item,
                    mapper.getUserIdByToken(),
                    Constants.STATUS_ACTIVE,
                    Constants.DONT_DELETE
            );

            if (cart == null) {
                // Xóa order nếu có bất kỳ sản phẩm nào không nằm trong giỏ hàng
                orderRepository.delete(order);
                throw new RuntimeException("Sản phẩm không nằm trong giỏ hàng");
            }
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setAccountId(accountId);
            orderDetail.setOrderId(order.getId());
            orderDetail.setProductId(cart.getProductId());
            orderDetail.setQuantity(cart.getQuantity());
            Optional<Product> productOptional = productRepository.findById(cart.getProductId());
// || productOptional.get().getQuantity() < item.getQuantity()
            if (productOptional.isEmpty() || productOptional.get().getStatus() == 0 || productOptional.get().getDeleted() == 0){
                List<OrderDetail> listOrderDetail = orderDetailRepository.findAllByOrderIdAndAccountIdAndStatusAndDeleted(
                        order.getId(),
                        mapper.getUserIdByToken(),
                        Constants.STATUS_ACTIVE,
                        Constants.DONT_DELETE

                );
//                orderDetailRepository.delete((OrderDetail) listOrderDetail);
                orderDetailRepository.deleteAllByOrderId(order.getId());
                orderRepository.delete(order);
//                throw new RuntimeException("status or delete của sản phẩm đang trong trạng thái 0");
            }
            Product product = productOptional.get();
//             Kiểm tra số lượng sản phẩm
            if (product.getQuantity() < cart.getQuantity()) {
                orderDetailRepository.deleteAllByOrderId(order.getId());
                orderRepository.delete(order);
                throw new RuntimeException("Sản phẩm không đủ số lượng: " + product.getName());
            }
            orderDetail.setPrice(product.getPrice());
            orderDetail.setStatus(Constants.STATUS_ACTIVE);
            orderDetail.setDeleted(Constants.DONT_DELETE);
            orderDetail.setCreatAt(new Date());
            orderDetail.setUpdateAt(new Date());
            Double price = product.getPrice()*cart.getQuantity();

            totalPrice += price;
            orderDetailRepository.save(orderDetail);

            product.setQuantity(productOptional.get().getQuantity() - cart.getQuantity());
            productRepository.save(product);

            cartRepository.delete(cart);
        }

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        return order;
    }

    @Override
    public Page<Order> getAllOrdersByUserId(Pageable pageable) {
        Page<Order> orderList = orderRepository.getAllCustom(
                mapper.getUserIdByToken(),
                Constants.DONT_DELETE,
                pageable);

        return orderList;
    }

    @Override
    public Page<Order> getOrdersByUserIdAndStatus(Integer status, Pageable pageable) {
        Page<Order> orderList = orderRepository.getAllByAccountAndStatus(
                mapper.getUserIdByToken(),
                status,
                Constants.DONT_DELETE,
                pageable);

        return orderList;
    }

    @Override
    public void cancelOrder(Long orderId) {
        Optional<Order> orderFind = orderRepository.findByIdAndDeletedAndAccountId(
                orderId,
                Constants.DONT_DELETE,
                mapper.getUserIdByToken()
        );
        if (orderFind.isEmpty()) {
            throw new RuntimeException("Không tìm thấy đơn hàng");
        }

        Order order = orderFind.get();
        if (order.getStatus() != Constants.CONFIRM) {
            throw new RuntimeException("Chỉ có thể hủy đơn hàng đang ở trạng thái xác nhận");
        }
        order.setStatus(Constants.DROP); // Hủy

        orderRepository.save(order);
    }

    @Override
    public void updateOrderStatus(Long orderId, Integer status) {
        Optional<Order> orderFind = orderRepository.findByIdAndDeletedAndAccountId(
                orderId,
                Constants.DONT_DELETE,
                mapper.getUserIdByToken()
        );

        if (orderFind.isEmpty()) {
            throw new RuntimeException("Không tìm thấy đơn hàng");
        }

        Order order = orderFind.get();
        if (order.getStatus() == Constants.DELIVERED && status == Constants.RECEIVED) {
            order.setStatus(status);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Không thể cập nhật trạng thái của đơn hàng này");
        }
    }

    @Override
    public void addFeedback(Long orderId, Long productId, String content, Integer rating) {

        Optional<Order> order = orderRepository.findByIdAndDeletedAndAccountId(orderId, Constants.DONT_DELETE, mapper.getUserIdByToken()); // check cac trang thai cua order

        Optional<OrderDetail> orderDetail = orderDetailRepository.findByOrderIdAndProductIdAndStatusAndDeleted(orderId, productId, Constants.STATUS_ACTIVE, Constants.DONT_DELETE); // check cac trang thai cua orderDetail

        if (order.isEmpty()) {
            throw new RuntimeException("không tìm thấy đơn hàng ");
        }
        if (orderDetail.isEmpty()){
            throw new RuntimeException("Sản phẩm không nằm trong đơn hàng");
        }

        Feedback feedback1 = new Feedback();
        feedback1.setOrderId(order.get().getId());
        feedback1.setContent(content);
        feedback1.setRating(rating);
        feedback1.setDeleted(Constants.DONT_DELETE);
        feedback1.setStatus(Constants.STATUS_ACTIVE);
        feedback1.setAccountId(mapper.getUserIdByToken());
        feedback1.setCreatAt(new Date());
        feedback1.setProductId(orderDetail.get().getProductId());
        if (order.get().getStatus() != Constants.RECEIVED) {
            throw new RuntimeException("Chỉ được đánh giá và phản hồi những đơn hàng đã nhận");
        }
        feedbackRepository.save(feedback1);
    }

    @Override
    public Order detail(Long orderId) {
        Order order = orderRepository.getOneCustom(
                orderId,
                mapper.getUserIdByToken(),
                Constants.DONT_DELETE
        );
        if (order == null) {
            throw new RuntimeException("Không tìm thấy đơn hàng");
        }
        return order;
    }
}
