package com.wisdom.roboticsecommerce.Repositories;

import com.wisdom.roboticsecommerce.Entities.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndDeleted(Long id, Integer delete);
    @Query(value = "select id,name from category c2 \n" +
            "\twhere c2.deleted = 1 \n" +
            "\tand :name is null or c2.name like concat('%',:name,'%') \n" +
            "\tand (:status is null or c2.status = :status)", nativeQuery = true)
    List<Map<String, Object>> search(@Param("name") String name, @Param("status") Integer status, Pageable pageable);
}