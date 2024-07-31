package com.wisdom.roboticsecommerce.Repositories;

import com.wisdom.roboticsecommerce.Entities.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM address a " +
            "WHERE (1 = 1) " +
                "AND (a.id = :addressId) " +
                "AND (a.account_id = :accountId) " +
                "AND (a.status = :status) " +
                "AND (a.deleted = :deleted) " +
            "LIMIT 1 ")
    Address getOneCustom(Long addressId, Long accountId, Integer status, Integer deleted);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM address a " +
            "WHERE (1 = 1) " +
                "AND (a.account_id = :accountId) " +
                "AND (a.status = :status) " +
                "AND (a.deleted = :deleted) ")
    Page<Address> getAllCustom(Long accountId, Integer status, Integer deleted, Pageable pageable);
}