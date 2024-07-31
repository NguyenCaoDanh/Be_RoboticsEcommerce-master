package com.wisdom.roboticsecommerce.Repositories;

import com.wisdom.roboticsecommerce.Entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM notification n " +
            "WHERE (1 = 1) " +
                "AND (n.account_id = :accountId) " +
                "AND (n.status = :status) " +
                "AND (n.deleted = :deleted) ")
    Page<Notification> getAllCustom (Long accountId, Integer status, Integer deleted, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM notification n " +
            "WHERE (1 = 1) " +
                "AND (n.id = :notificationId) " +
                "AND (n.account_id = :accountId) " +
                "AND (n.status = :status) " +
                "AND (n.deleted = :deleted) " +
            "LIMIT 1 ")
    Notification getOneCustom(Long notificationId, Long accountId, Integer status, Integer deleted);
}