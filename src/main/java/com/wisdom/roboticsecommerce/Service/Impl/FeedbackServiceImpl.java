package com.wisdom.roboticsecommerce.Service.Impl;

import com.wisdom.roboticsecommerce.Dto.FeedbackDto;
import com.wisdom.roboticsecommerce.Dto.FeedbackProductResponse;
import com.wisdom.roboticsecommerce.Entities.Account;
import com.wisdom.roboticsecommerce.Entities.Feedback;
import com.wisdom.roboticsecommerce.Entities.Order;
import com.wisdom.roboticsecommerce.Entities.Product;
import com.wisdom.roboticsecommerce.ExceptionHandler.CustomException;
import com.wisdom.roboticsecommerce.Mapper.Mapper;
import com.wisdom.roboticsecommerce.Repositories.*;
import com.wisdom.roboticsecommerce.Service.FeedbackService;
import com.wisdom.roboticsecommerce.Utils.Constants;
import com.wisdom.roboticsecommerce.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private Mapper mapper;

    @Override
    public Feedback create(FeedbackDto feedbackDto) {
        // check exists feedback
        Boolean existsFeedback = feedbackRepository.existsByOrderIdAndProductIdAndAccountId(
                feedbackDto.getOrderId(),
                feedbackDto.getProductId(),
                mapper.getUserIdByToken()
        );
        if (existsFeedback) {
            throw new CustomException("Chỉ được đánh giá 1 lần");
        }

        // check rating field
        if (feedbackDto.getRating() < 1 || feedbackDto.getRating() > 5) {
            throw new CustomException("Dữ liệu nhập không hợp lệ");
        }

        // check order status
        Order order = orderRepository.getOneCustom(
                feedbackDto.getOrderId(),
                mapper.getUserIdByToken(),
                Constants.DONT_DELETE
        );
        if (order == null) {
            throw new CustomException("Không tìm thấy đơn hàng");
        }
        if (order.getStatus() != Constants.RECEIVED) {
            throw new CustomException("Chỉ được đánh giá / phản hồi đơn hàng có trạng thái đã nhận");
        }

        // check product in order detail
        Boolean existsOrderDetail = orderDetailRepository.existsByOrderIdAndProductIdAndAccountId(
                feedbackDto.getOrderId(),
                feedbackDto.getProductId(),
                mapper.getUserIdByToken()
        );
        if (!existsOrderDetail) {
            throw new CustomException("Không tìm thấy sản phẩm trong đơn hàng");
        }

        // check product
        Optional<Product> product = productRepository.findById(feedbackDto.getProductId());
        if (product.isEmpty() || product.get().getDeleted() == Constants.DELETED) {
            throw new CustomException("Không tìm thấy sản phẩm trong kho");
        }

        Feedback feedback = new Feedback();
        feedback.setProductId(feedbackDto.getProductId());
        feedback.setOrderId(feedbackDto.getOrderId());
        feedback.setContent(feedbackDto.getContent());
        feedback.setRating(feedbackDto.getRating());

        feedback.setCreatAt(new Date());
        feedback.setStatus(Constants.STATUS_ACTIVE);
        feedback.setDeleted(Constants.DONT_DELETE);
        feedback.setAccountId(mapper.getUserIdByToken());

        Feedback feedbackCreate = feedbackRepository.save(feedback);

        return feedbackCreate;
    }

    @Override
    public Feedback createReply(Long feedbackId, String reply) {
        Feedback feedback = getDetail(feedbackId);

        feedback.setReply(reply);
        feedback.setUpdateAt(new Date());

        Feedback feedbackUpdate = feedbackRepository.save(feedback);
        return feedbackUpdate;
    }

    @Override
    public Feedback getDetail(Long feedbackId) {
        Feedback feedback = feedbackRepository.getOneCustom(feedbackId, Constants.DONT_DELETE);
        if (feedback == null) {
            throw new CustomException("Không tìm thấy bình luận / phản hồi với id " + feedbackId);
        }
        return feedback;
    }

    @Override
    public Feedback updateStatus(Long feedbackId, Integer status) {
        Feedback feedback = getDetail(feedbackId);

        if (status != Constants.STATUS_ACTIVE && status != Constants.STATUS_INACTIVE) {
            throw new CustomException("Dữ liệu nhâập vào không hợp lệ");
        }

        feedback.setStatus(status);
        feedback.setUpdateAt(new Date());

        Feedback feedbackUpdate = feedbackRepository.save(feedback);
        return feedbackUpdate;
    }

    @Override
    public Feedback updateReply(Long feedbackId, String reply) {
        Feedback feedback = getDetail(feedbackId);
        feedback.setReply(reply);

        Long accountId = mapper.getUserIdByToken();
        feedback.setAccountId(accountId);
        feedback.setUpdateAt(new Date());
        Feedback feedbackUpdate = feedbackRepository.save(feedback);
        return feedbackUpdate;
    }

    @Override
    public void deleteReply(Long feedbackId) {
        Feedback feedback = getDetail(feedbackId);
        feedback.setReply(null);

        Long accountId = mapper.getUserIdByToken();
        feedback.setAccountId(accountId);
        feedback.setUpdateAt(new Date());
        feedbackRepository.save(feedback);
    }

    @Override
    public Page<Feedback> getAll(Integer status, Pageable pageable) {
        Page<Feedback> feedbackList = feedbackRepository.getAllCustom(status, pageable);

        return feedbackList;
    }

    @Override
    public Page<FeedbackProductResponse> getAllByProduct(Long productId, Pageable pageable) {
        Page<Feedback> feedbackList = feedbackRepository.findAllByProductIdAndStatusAndDeleted(
                productId,
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE,
                pageable
        );

        List<FeedbackProductResponse> feedbackProductResponseList = new ArrayList<>();
        for (Feedback item : feedbackList.getContent()) {
            FeedbackProductResponse feedbackProductResponse = new FeedbackProductResponse();
            Utils.modelMapper(false, false).map(item, feedbackProductResponse);

            Optional<Account> account = accountRepository.findById(item.getAccountId());
            if (account.isEmpty()) {
                feedbackProductResponse.setFullName("Unknown");
            } else {
                feedbackProductResponse.setFullName(account.get().getFullname());
            }

            feedbackProductResponseList.add(feedbackProductResponse);
        }

        Page<FeedbackProductResponse> feedbackProductResponsePage = new PageImpl<>(feedbackProductResponseList, pageable, feedbackProductResponseList.size());

        return feedbackProductResponsePage;
    }
}
