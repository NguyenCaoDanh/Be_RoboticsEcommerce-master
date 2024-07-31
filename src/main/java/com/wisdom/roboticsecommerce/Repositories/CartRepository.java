package com.wisdom.roboticsecommerce.Repositories;

import com.wisdom.roboticsecommerce.Dto.ListCartRequest;
import com.wisdom.roboticsecommerce.Entities.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByAccountIdAndProductIdAndStatusAndDeleted(Long accountId, Long productId, Integer status, Integer deleted);
    Optional<Cart> findByIdAndAccountId(Long id, Long accountId);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM cart c " +
            "WHERE (1 = 1) " +
                "AND c.id = :cartId " +
                "AND c.account_id = :accountId " +
                "AND c.status = :status " +
                "AND c.deleted = :deleted " +
            "LIMIT 1")
    Cart getOneCustom(Long cartId, long accountId, Integer status, Integer deleted);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM cart c " +
            "WHERE (1 = 1) " +
                "AND c.account_id = :accountId " +
                "AND c.status = :status " +
                "AND c.deleted = :deleted ")
    Page<Cart> getAllCustom(long accountId, Integer status, Integer deleted, Pageable pageable);
}