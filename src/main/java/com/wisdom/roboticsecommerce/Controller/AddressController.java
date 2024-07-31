package com.wisdom.roboticsecommerce.Controller;

import com.wisdom.roboticsecommerce.Entities.Address;
import com.wisdom.roboticsecommerce.Mapper.PageMapper;
import com.wisdom.roboticsecommerce.Dto.PageableDto;
import com.wisdom.roboticsecommerce.Service.AddressService;
import com.wisdom.roboticsecommerce.Utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/address")
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private PageMapper pageMapper;

    @PostMapping("create")
    private ResponseEntity<?> create(@RequestBody Address address) {
        Address addressCreate = addressService.create(address);

        return ResponseUtil.success(addressCreate);
    }

    @PutMapping("update")
    private ResponseEntity<?> update(@RequestBody Address address) {
        Address addressUpdate = addressService.update(address);

        return ResponseUtil.success(addressUpdate);
    }

    @GetMapping("all")
    private ResponseEntity<?> getAll(@ModelAttribute PageableDto pageableDto) {
        Pageable pageable = pageMapper.customPage(
                pageableDto.getPageNo(),
                pageableDto.getPageSize(),
                pageableDto.getSortBy().isEmpty() ? "id" : pageableDto.getSortBy(),
                pageableDto.getSortType().isEmpty() ? "desc" : pageableDto.getSortType());
        Page<Address> addressList = addressService.getAllByCurrentAccount(pageable);

        return ResponseUtil.success(addressList);
    }

    @GetMapping("detail")
    private ResponseEntity<?> getDetail(@RequestParam Long addressId) {
        Address address = addressService.getDetail(addressId);

        return ResponseUtil.success(address);
    }

    @DeleteMapping("delete")
    private ResponseEntity<?> delete(@RequestParam Long addressId) {
        addressService.delete(addressId);

        return ResponseUtil.success("Xóa địa chỉ thành công");
    }
}
