package com.wisdom.roboticsecommerce.Repositories;

import com.wisdom.roboticsecommerce.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndDeleted(Long productId, Integer dontDelete);
    @Query(value = "SELECT " +
            "    p.id," +
            "    p.category_id, " +
            "    p.code," +
            "    p.description," +
            "    p.name, p.price," +
            "    p.promotion_id, p.quantity," +
            "    pi2.image " +
            "FROM product p " +
            "LEFT JOIN product_image pi2 ON pi2.product_id = p.id " +
            "LEFT JOIN promotion p2 ON p2.id = p.promotion_id " +
            "WHERE (p.deleted = 1 and (:categoryId IS NULL OR p.category_id = :categoryId))" +
            "\tand :name is null or p.name like CONCAT('%',:name, '%') and (:status is null or p.status = :status)",nativeQuery = true)
    Page<Map<String,Object>> search(@Param("categoryId") Long category_id, @Param("name") String name, @Param("status") Integer status, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM product p " +
            "WHERE (1 = 1) " +
                "AND p.id = :productId " +
                "AND p.status = :status " +
                "AND p.deleted = :deleted " +
            "LIMIT 1 ")
    Product getOneCustom(Long productId, Integer status, Integer deleted);

    @Query(nativeQuery = true, value = "SELECT " +
            "    a.id," +
            "    a.category_id, " +
            "    a.code," +
            "    a.description," +
            "    a.name, " +
            "    a.price," +
            "    a.promotion_id, " +
            "    a.quantity," +
            "    pi.image " +
            "FROM product a " +
            "LEFT JOIN product_image pi ON a.id = pi.product_id " +
            "WHERE (1 = 1) " +
                "AND a.status = :status " +
                "AND a.deleted = :deleted " +
                "AND ((1 = 2) " +
                    "OR ((:categoryId IS NOT NULL) AND (a.category_id = :categoryId)) " +
                    "OR ((:categoryId IS NULL) AND (1 = 1))) " +
                "AND ((1 = 2) " +
                    "OR (a.name LIKE %:keyword%) " +
                    "OR (a.description LIKE %:keyword%)) ")
    Page<Map<String,Object>> findByKeyword(String keyword, Long categoryId, Integer status, Integer deleted, Pageable pageable);
}