package com.wisdom.roboticsecommerce.Service;

import com.wisdom.roboticsecommerce.Entities.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressService {
    Address create(Address address);
    Address update(Address address);
    Page<Address> getAllByCurrentAccount(Pageable pageable);
    Address getDetail(Long addressId);
    void delete(Long addressId);
}
