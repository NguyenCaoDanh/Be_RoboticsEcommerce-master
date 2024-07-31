package com.wisdom.roboticsecommerce.Service.Impl;

import com.wisdom.roboticsecommerce.Entities.Address;
import com.wisdom.roboticsecommerce.ExceptionHandler.CustomException;
import com.wisdom.roboticsecommerce.Mapper.Mapper;
import com.wisdom.roboticsecommerce.Repositories.AddressRepository;
import com.wisdom.roboticsecommerce.Service.AddressService;
import com.wisdom.roboticsecommerce.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private Mapper mapper;

    @Override
    public Address create(Address address) {
        // check null
        if (1 == 2
                || (address.getFullname() == null || address.getFullname().trim().equals(""))
                || (address.getPhone() == null || address.getPhone().trim().equals(""))
                || (address.getTag_name() == null || address.getTag_name().trim().equals(""))
                || (address.getProvinceId() == null)
                || (address.getDistrictId() == null)
                || (address.getWardId() == null)
                || (address.getAddress_detail() == null || address.getAddress_detail().trim().equals(""))) {
            throw new CustomException("Dữ liệu nhập vào bị bỏ trống");
        }

        // set default values
        address.setAccountId(mapper.getUserIdByToken());
        address.setCreatAt(new Date());
        address.setStatus(Constants.STATUS_ACTIVE);
        address.setDeleted(Constants.DONT_DELETE);

        Address addressCreate = addressRepository.save(address);

        return addressCreate;
    }

    @Override
    public Address update(Address address) {
        // check null
        if (1 == 2
                || (address.getFullname() == null || address.getFullname().trim().equals(""))
                || (address.getPhone() == null || address.getPhone().trim().equals(""))
                || (address.getTag_name() == null || address.getTag_name().trim().equals(""))
                || (address.getProvinceId() == null)
                || (address.getDistrictId() == null)
                || (address.getWardId() == null)
                || (address.getAddress_detail() == null || address.getAddress_detail().trim().equals(""))) {
            throw new CustomException("Dữ liệu nhập vào bị bỏ trống");
        }

        // update value
        Address addressFound = getDetail(address.getId());
        addressFound.setFullname(address.getFullname());
        addressFound.setPhone(address.getPhone());
        addressFound.setTag_name(address.getTag_name());
        addressFound.setProvinceId(address.getProvinceId());
        addressFound.setDistrictId(address.getDistrictId());
        addressFound.setWardId(address.getWardId());
        addressFound.setAddress_detail(address.getAddress_detail());

        // set default values
        addressFound.setUpdateAt(new Date());

        Address addressCreate = addressRepository.save(addressFound);

        return addressCreate;
    }

    @Override
    public Page<Address> getAllByCurrentAccount(Pageable pageable) {
        Page<Address> addressList = addressRepository.getAllCustom(
                mapper.getUserIdByToken(),
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE,
                pageable);

        return addressList;
    }

    @Override
    public Address getDetail(Long addressId) {
        Long accountId = mapper.getUserIdByToken();
        Address address = addressRepository.getOneCustom(
                addressId,
                accountId,
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE);

        if (address == null) {
            throw new CustomException("Không tìm thấy địa chỉ với id " + addressId);
        }

        return address;
    }

    @Override
    public void delete(Long addressId) {
        Address address = getDetail(addressId);

        address.setDeleted(Constants.DELETED);
        address.setUpdateAt(new Date());
        address.setStatus(Constants.STATUS_INACTIVE);

        addressRepository.save(address);
    }
}
