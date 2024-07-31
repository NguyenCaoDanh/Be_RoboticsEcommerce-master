package com.wisdom.roboticsecommerce.Repositories;

import com.wisdom.roboticsecommerce.Entities.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM feedback f " +
            "WHERE (1 = 1) " +
                "AND (f.id = :feedbackId) " +
                "AND (f.deleted = :deleted) " +
            "LIMIT 1 ")
    Feedback getOneCustom(Long feedbackId, Integer deleted);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM feedback f " +
            "WHERE (1 = 2) " +
                "OR ((:status IS NOT NULL) AND (f.status = :status))" +
                "OR ((:status IS NULL) AND (1 = 1)) ")
    Page<Feedback> getAllCustom(Integer status, Pageable pageable);

    Boolean existsByOrderIdAndProductIdAndAccountId(Long orderId, Long productId, Long accountId);

    Page<Feedback> findAllByProductIdAndStatusAndDeleted(Long productId, Integer status, Integer deleted, Pageable pageable);
}