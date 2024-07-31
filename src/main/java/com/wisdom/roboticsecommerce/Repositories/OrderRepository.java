package com.wisdom.roboticsecommerce.Repositories;

import com.wisdom.roboticsecommerce.Dto.RevenueResponse;
import com.wisdom.roboticsecommerce.Entities.Order;
import com.wisdom.roboticsecommerce.Entities.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import java.util.HashMap;
import java.util.List;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndDeleted(Long id, Integer delete);
    @Query(value = "select * from orders o2 \n" +
            "\tleft join order_detail od on od.order_id = o2.id where :status is null or o2.status = :status \n" +
            "\tand o2.deleted = 1", nativeQuery = true)
    Page<Map<String, Object>> search(@Param("status") Integer status, Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT 'year' as unitName , YEAR(o.create_at) as unitValue, SUM(o.total_price) as revenue " +
            "FROM orders o " +
            "GROUP BY YEAR(o.create_at) " +
            "ORDER BY YEAR(o.create_at) ASC ")
    List<Object[]> getRevenueByYear();

    @Query(nativeQuery = true, value = "SELECT 'month' as unitName, MONTH(o.create_at) as unitValue, SUM(o.total_price) as revenue " +
            "FROM orders o " +
            "WHERE YEAR(o.create_at) = :year " +
            "GROUP BY MONTH(create_at) " +
            "ORDER BY MONTH(create_at) ASC ")
    List<Object[]> getRevenueByMonth(Integer year);

    @Query(nativeQuery = true, value = "SELECT 'day' as unitName, DAY(o.create_at) as unitValue, SUM(o.total_price) as revenue " +
            "FROM orders o " +
            "WHERE YEAR(o.create_at) = :year AND MONTH(o.create_at) = :month " +
            "GROUP BY DAY(create_at) " +
            "ORDER BY DAY(create_at) ASC ")
    List<Object[]> getRevenueByDay(Integer year, Integer month);


    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM orders a " +
            "WHERE (1 = 1) " +
            "AND (a.account_id = :accountId) " +
            "AND (a.deleted = :deleted) ")
    Page<Order> getAllCustom(Long accountId, Integer deleted, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM orders o " +
            "WHERE (1 = 1) " +
                "AND o.account_id = :accountId " +
                "AND o.status = :status " +
                "AND o.deleted = :deleted ")
    Page<Order> getAllByAccountAndStatus(Long accountId, Integer status, Integer deleted, Pageable pageable);

    Optional<Order> findByIdAndDeletedAndAccountId(Long id, Integer deleted, Long accountId);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM orders o " +
            "WHERE (1 = 1) " +
                "AND o.id = :orderId " +
                "AND o.account_id = :accountId " +
                "AND o.deleted = :deleted ")
    Order getOneCustom(Long orderId, Long accountId, Integer deleted);
}