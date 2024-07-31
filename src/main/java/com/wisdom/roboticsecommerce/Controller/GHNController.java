package com.wisdom.roboticsecommerce.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.roboticsecommerce.Dto.GHNCalculateFeeRequest;
import com.wisdom.roboticsecommerce.Dto.GHNOrderPrintRequest;
import com.wisdom.roboticsecommerce.Dto.GHNOrderRequest;
import com.wisdom.roboticsecommerce.Entities.Shipping;
import com.wisdom.roboticsecommerce.Service.GHNService;
import com.wisdom.roboticsecommerce.Utils.ResponseUtil;
import com.wisdom.roboticsecommerce.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/ghn")
public class GHNController {
    @Autowired
    private GHNService ghnService;

    @GetMapping("address/province/all")
    private ResponseEntity<?> getAllProvince() {
        JsonNode jsonNode = ghnService.getProvince();

        return ResponseUtil.success(jsonNode);
    }

    @GetMapping("address/province/district/all")
    private ResponseEntity<?> getDistrictByProvince(@RequestParam Long provinceId) {
        JsonNode jsonNode = ghnService.getDistrictByProvince(provinceId);

        return ResponseUtil.success(jsonNode);
    }

    @GetMapping("address/province/district/ward/all")
    private ResponseEntity<?> getWardByDistrict(@RequestParam Long districtId) {
        JsonNode jsonNode = ghnService.getWardByDistrict(districtId);

        return ResponseUtil.success(jsonNode);
    }

    @GetMapping("service/all")
    private ResponseEntity<?> getService(@RequestParam Long toDistrictId) {
        JsonNode jsonNode = ghnService.getService(toDistrictId);

        return ResponseUtil.success(jsonNode);
    }

    @GetMapping("fee-and-delivery-time/calculate")
    private ResponseEntity<?> calculateFee(@ModelAttribute GHNCalculateFeeRequest request) {
        JsonNode fee = ghnService.calculateFee(request);
        JsonNode deleverryTime = ghnService.calculateDeliveryTime(
                request.getToDistrictId(),
                request.getToWardCode(),
                request.getServiceId()
        );

        Map<String, Object> respone = new HashMap<>();
        respone.put("total", fee.get("total").asLong());
        respone.put("expected_delivery_time", new Date(deleverryTime.get("leadtime").asLong() * 1000));

        return ResponseUtil.success(respone);
    }

    @GetMapping("order/soc")
    private ResponseEntity<?> getFeeByShippingOrder(@RequestParam String shippingOrderCode) {
        JsonNode jsonNode = ghnService.getFeeByShippingOrder(shippingOrderCode);

        return ResponseUtil.success(jsonNode);
    }

    @PostMapping("order/create")
    private ResponseEntity<?> createOrder(@RequestBody GHNOrderRequest request) {
        Shipping shipping = ghnService.createOrder(request);

        return ResponseUtil.success(shipping);
    }

    @GetMapping("store/detail")
    private ResponseEntity<?> getStoreDetail() {
        JsonNode jsonNode = ghnService.getStoreDetail();

        return ResponseUtil.success(jsonNode);
    }

    @GetMapping("order/status")
    private ResponseEntity<?> getOrderDetail(@RequestParam String shippingOrderCode) {
        JsonNode jsonNode = ghnService.getOrderStatus(shippingOrderCode);

        return ResponseUtil.success(jsonNode);
    }

    @DeleteMapping("order/cancel")
    private ResponseEntity<?> cancelOrder(@RequestParam String shippingOrderCode) {
        ghnService.cancelOrder(shippingOrderCode);

        return ResponseUtil.success("Hủy đơn hàng vận chuyển thành công");
    }

    @PostMapping("order/print")
    private ResponseEntity<?> printOrder(@RequestBody GHNOrderPrintRequest request) {
        String token = ghnService.printOrder(request.getShippingOrderCode());
        String a5 = "https://dev-online-gateway.ghn.vn/a5/public-api/printA5?token=";
        String x80 = "https://dev-online-gateway.ghn.vn/a5/public-api/print80x80?token=";
        String x50 = "https://dev-online-gateway.ghn.vn/a5/public-api/print52x70?token=";

        Map<String, Object> response = new HashMap<>();
        response.put("print_a5", a5 + token);
        response.put("print_80x80", x80 + token);
        response.put("print_50x72", x50 + token);

        return ResponseUtil.success(response);
    }
}
