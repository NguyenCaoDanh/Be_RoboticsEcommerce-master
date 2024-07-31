package com.wisdom.roboticsecommerce.Repositories;

import com.wisdom.roboticsecommerce.Entities.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String username);
    @Transactional
    @Modifying
    @Query(value = " insert into user_role (user_id,role_id) values (:userId,:roleId)",nativeQuery = true)
    void create(Long userId,Long roleId);

    Account findByToken(String token);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM account a " +
            "WHERE (1 = 1) " +
                "AND ((1 = 2) " +
                    "OR ((:status IS NOT NULL) AND (a.status = :status))" +
                    "OR ((:status IS NULL) AND (1 = 1))) " +
                "AND ((1 = 2) " +
                    "OR (a.birthday LIKE %:keyword%) " +
                    "OR (a.create_at LIKE %:keyword%) " +
    //                "OR (a.deleted LIKE %:keyword%) " +
                    "OR (a.email LIKE %:keyword%) " +
                    "OR (a.full_name LIKE %:keyword%) " +
    //                "OR (a.gender LIKE %:keyword%) " +
//                    "OR (a.status = :status) " +
                    "OR (a.update_at LIKE %:keyword%)) ")
    Page<Account> findByKeyword(String keyword, Integer status, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM account a " +
            "WHERE (1 = 1) " +
                "AND (a.id = :accountId) " +
                "AND (a.status = :status) " +
                "AND (a.deleted = :deleted) " +
            "LIMIT 1 ")
    Account getOneCustom(Long accountId, Integer status, Integer deleted);
}