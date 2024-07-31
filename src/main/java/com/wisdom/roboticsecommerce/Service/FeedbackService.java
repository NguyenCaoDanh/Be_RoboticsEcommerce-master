package com.wisdom.roboticsecommerce.Service;

import com.wisdom.roboticsecommerce.Dto.FeedbackDto;
import com.wisdom.roboticsecommerce.Dto.FeedbackProductResponse;
import com.wisdom.roboticsecommerce.Entities.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackService {
    Feedback create(FeedbackDto feedbackDto);
    Feedback createReply(Long feedbackId, String reply);
    Feedback getDetail(Long feedbackId);
    Feedback updateStatus(Long feedbackId, Integer status);
    Feedback updateReply(Long feedbackId, String reply);
    void deleteReply(Long feedbackId);
    Page<Feedback> getAll(Integer status, Pageable pageable);
    Page<FeedbackProductResponse> getAllByProduct(Long productId, Pageable pageable);
}
