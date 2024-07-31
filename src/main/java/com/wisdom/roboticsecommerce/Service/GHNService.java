package com.wisdom.roboticsecommerce.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.wisdom.roboticsecommerce.Dto.GHNCalculateFeeRequest;
import com.wisdom.roboticsecommerce.Dto.GHNOrderRequest;
import com.wisdom.roboticsecommerce.Entities.Shipping;

import java.util.Date;

public interface GHNService {
    JsonNode getProvince();
    JsonNode getDistrictByProvince(Long provinceId);
    JsonNode getWardByDistrict(Long districtId);
    JsonNode getService(Long toDistrictId);
    JsonNode calculateFee(GHNCalculateFeeRequest request);
    JsonNode getFeeByShippingOrder(String shippingOrderCode);
    Shipping createOrder(GHNOrderRequest request);
    JsonNode getStoreDetail();
    JsonNode getOrderStatus(String orderCode);
    JsonNode calculateDeliveryTime(Long toDistrictId, String toWardCode, Long serviceId);
    void cancelOrder(String orderCode);
    String printOrder(String orderCode);
}
