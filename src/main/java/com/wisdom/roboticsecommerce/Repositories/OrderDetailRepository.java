package com.wisdom.roboticsecommerce.Repositories;

import com.wisdom.roboticsecommerce.Entities.Order;
import com.wisdom.roboticsecommerce.Entities.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM order_detail a " +
            "WHERE (1 = 1) " +
            "AND (a.id = :orderId) " +
            "AND (a.account_id = :accountId) " +
            "AND (a.status = :status) " +
            "AND (a.deleted = :deleted) " +
            "LIMIT 1 ")
    OrderDetail getOneCustom(Long orderId, Long accountId, Integer status, Integer deleted);
    Optional<OrderDetail> findByOrderIdAndProductIdAndStatusAndDeleted(Long orderId, Long productId, Integer status, Integer deleted);


    List<OrderDetail> findAllByOrderIdAndAccountIdAndStatusAndDeleted(Long orderId, Long accountId, Integer status, Integer deleted);

    void deleteAllByOrderId(Long id);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM order_detail od " +
            "WHERE (1 = 1) " +
                "AND od.order_id = :orderId " +
                "AND od.status = :status " +
                "AND od.deleted = :deleted " +
                "AND od.account_id = :accountId ")
    Page<OrderDetail> getAllCustom(Long orderId, Integer status, Integer deleted, Long accountId, Pageable pageable);

    Boolean existsByOrderIdAndProductIdAndAccountId(Long orderId, Long productId, Long accountId);
}