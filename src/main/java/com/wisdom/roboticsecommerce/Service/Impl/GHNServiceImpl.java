package com.wisdom.roboticsecommerce.Service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wisdom.roboticsecommerce.Dto.APIRequest;
import com.wisdom.roboticsecommerce.Dto.GHNCalculateFeeRequest;
import com.wisdom.roboticsecommerce.Dto.GHNOrderRequest;
import com.wisdom.roboticsecommerce.Entities.Shipping;
import com.wisdom.roboticsecommerce.ExceptionHandler.CustomException;
import com.wisdom.roboticsecommerce.ExceptionHandler.LogOnlyException;
import com.wisdom.roboticsecommerce.Mapper.Mapper;
import com.wisdom.roboticsecommerce.Repositories.ShippingRepository;
import com.wisdom.roboticsecommerce.Service.APIService;
import com.wisdom.roboticsecommerce.Service.GHNService;
import com.wisdom.roboticsecommerce.Utils.Constants;
import com.wisdom.roboticsecommerce.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GHNServiceImpl implements GHNService {
    @Autowired
    private APIService apiService;
    @Autowired
    private ShippingRepository shippingRepository;
    @Autowired
    private Mapper mapper;

    @Value("${ghn.token}")
    private String token;
    @Value("${ghn.shop.id}")
    private Long shopId;
    @Value("${ghn.shop.phone-number}")
    private String phoneNumber;

    @Override
    public JsonNode getProvince() {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);

//        Map<String, Object> body = new HashMap<>();
//        body.put("key", "value");

//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("paramKey", "paramValue");

        APIRequest apiRequest = new APIRequest(
                url,
                HttpMethod.GET,
                headers,
                null,
                null
        );
        ResponseEntity<?> response = apiService.call(apiRequest);
        JsonNode jsonNode = Utils.convertToJson(response.getBody());
        HttpStatusCode status = response.getStatusCode();
        if (status == HttpStatus.OK) {
            return jsonNode.get("data");
        } else if (status == HttpStatus.BAD_REQUEST) {
            System.out.println("call ghn api error: " + response.toString());
            throw new CustomException(jsonNode.get("message").asText());
        } else {
            throw new LogOnlyException(response.toString());
        }
    }

    @Override
    public JsonNode getDistrictByProvince(Long provinceId) {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);

        Map<String, Object> body = new HashMap<>();
        body.put("province_id", provinceId);

        APIRequest apiRequest = new APIRequest(
                url,
                HttpMethod.POST,
                headers,
                null,
                body
        );
        ResponseEntity<?> response = apiService.call(apiRequest);
        JsonNode jsonNode = Utils.convertToJson(response.getBody());
        HttpStatusCode status = response.getStatusCode();
        if (status == HttpStatus.OK) {
            return jsonNode.get("data");
        } else if (status == HttpStatus.BAD_REQUEST) {
            System.out.println("call ghn api error: " + response.toString());
            throw new CustomException(jsonNode.get("message").asText());
        } else {
            throw new LogOnlyException(response.toString());
        }
    }

    @Override
    public JsonNode getWardByDistrict(Long districtId) {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);

        Map<String, Object> body = new HashMap<>();
        body.put("district_id", districtId);

        APIRequest apiRequest = new APIRequest(
                url,
                HttpMethod.POST,
                headers,
                null,
                body
        );
        ResponseEntity<?> response = apiService.call(apiRequest);
        JsonNode jsonNode = Utils.convertToJson(response.getBody());
        HttpStatusCode status = response.getStatusCode();
        if (status == HttpStatus.OK) {
            return jsonNode.get("data");
        } else if (status == HttpStatus.BAD_REQUEST) {
            System.out.println("call ghn api error: " + response.toString());
            throw new CustomException(jsonNode.get("message").asText());
        } else {
            throw new LogOnlyException(response.toString());
        }
    }

    @Override
    public JsonNode getService(Long toDistrictId) {
        JsonNode store = getStoreDetail();

        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);

        Map<String, Object> body = new HashMap<>();
        body.put("shop_id", shopId);
        body.put("from_district", store.get("district_id").asLong());
        body.put("to_district", toDistrictId);

        APIRequest apiRequest = new APIRequest(
                url,
                HttpMethod.POST,
                headers,
                null,
                body
        );
        ResponseEntity<?> response = apiService.call(apiRequest);
        JsonNode jsonNode = Utils.convertToJson(response.getBody());
        HttpStatusCode status = response.getStatusCode();
        if (status == HttpStatus.OK) {
            List<Object> serviceList = new ArrayList<>();
            serviceList.add(Utils.jsonNodeFindByField(jsonNode.get("data"), "service_type_id", "2"));

            return Utils.convertToJson(serviceList);
        } else if (status == HttpStatus.BAD_REQUEST) {
            System.out.println("call ghn api error: " + response.toString());
            throw new CustomException(jsonNode.get("message").asText());
        } else {
            throw new LogOnlyException(response.toString());
        }
    }

    @Override
    public JsonNode calculateFee(GHNCalculateFeeRequest request) {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);
        headers.set("ShopId", String.valueOf(shopId));

        Map<String, Object> body = new HashMap<>();
        body.put("service_id", request.getServiceId());
        body.put("insurance_value", request.getOrderValue());
//        body.put("coupon", request.getCoupon());
        body.put("to_district_id", request.getToDistrictId());
        body.put("to_ward_code", request.getToWardCode());
        body.put("weight", request.getWeight());
        body.put("length", request.getLength());
        body.put("width", request.getWidth());
        body.put("height", request.getHeight());
        body.put("cod_value", request.getOrderValue());

        APIRequest apiRequest = new APIRequest(
                url,
                HttpMethod.POST,
                headers,
                null,
                body
        );
        ResponseEntity<?> response = apiService.call(apiRequest);
        JsonNode jsonNode = Utils.convertToJson(response.getBody());
        HttpStatusCode status = response.getStatusCode();
        if (status == HttpStatus.OK) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.put("total", jsonNode.get("data").get("total"));
            return rootNode;
        } else if (status == HttpStatus.BAD_REQUEST) {
            System.out.println("call ghn api error: " + response.toString());
            throw new CustomException(jsonNode.get("message").asText());
        } else {
            throw new LogOnlyException(response.toString());
        }
    }

    @Override
    public JsonNode getFeeByShippingOrder(String shippingOrderCode) {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/soc";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);
        headers.set("ShopID", String.valueOf(shopId));

        Map<String, Object> body = new HashMap<>();
        body.put("order_code", shippingOrderCode);

        APIRequest apiRequest = new APIRequest(
                url,
                HttpMethod.POST,
                headers,
                null,
                body
        );
        ResponseEntity<?> response = apiService.call(apiRequest);
        JsonNode jsonNode = Utils.convertToJson(response.getBody());
        HttpStatusCode status = response.getStatusCode();
        if (status == HttpStatus.OK) {
            return jsonNode.get("data");
        } else if (status == HttpStatus.BAD_REQUEST) {
            System.out.println("call ghn api error: " + response.toString());
            throw new CustomException(jsonNode.get("message").asText());
        } else {
            throw new LogOnlyException(response.toString());
        }
    }

    @Override
    public Shipping createOrder(GHNOrderRequest request) {
        JsonNode store = getStoreDetail();

        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);
        headers.set("ShopId", String.valueOf(shopId));

        List<Object> itemList = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("name", "test");
        item.put("code", "test");
        item.put("quantity", 1);
        item.put("weight", 200);
        item.put("length", 10);
        item.put("width", 10);
        item.put("height",10 );
        itemList.add(item);

        Map<String, Object> body = new HashMap<>();
        body.put("payment_type_id", 2);
        body.put("note", request.getNote());
        body.put("required_note", request.getRequiredNote());
        body.put("to_name", request.getToName());
        body.put("to_phone", request.getToPhone());
        body.put("to_address", request.getToAddress());
        body.put("to_ward_code", request.getToWardCode());
        body.put("to_district_id", request.getToDistrictId());
        body.put("cod_amount", request.getCodAmount());
        body.put("content", request.getContent());
        body.put("weight", request.getWeight());
        body.put("length", request.getLength());
        body.put("width", request.getWidth());
        body.put("height", request.getHeight());
        body.put("service_id", request.getServiceId());
        body.put("service_type_id", request.getServiceTypeId());
        body.put("items", itemList);

        APIRequest apiRequest = new APIRequest(
                url,
                HttpMethod.POST,
                headers,
                null,
                body
        );
        ResponseEntity<?> response = apiService.call(apiRequest);
        JsonNode jsonNode = Utils.convertToJson(response.getBody());
        HttpStatusCode status = response.getStatusCode();
        if (status == HttpStatus.OK) {
            Long accountId = mapper.getUserIdByToken();

            Shipping shipping = new Shipping();
            shipping.setOrderId(request.getOrderId());
            shipping.setShippingCode(jsonNode.get("data").get("order_code").asText());
            try {
                shipping.setExpected_delivery_time(
                        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(jsonNode.get("data").get("expected_delivery_time").asText())
                );
            } catch (ParseException e) {
                throw new LogOnlyException(e.getMessage());
            }
            shipping.setTotalFree(jsonNode.get("data").get("total_fee").asDouble());
            shipping.setTrans_type(jsonNode.get("data").get("trans_type").asText());
            shipping.setStatus(Constants.STATUS_ACTIVE);
            shipping.setAccountId(accountId);
            shipping.setCreatAt(new Date());
            shipping.setDeleted(Constants.DONT_DELETE);
            shippingRepository.save(shipping);

            return shipping;
        } else if (status == HttpStatus.BAD_REQUEST) {
            System.out.println("call ghn api error: " + response.toString());
            throw new CustomException(jsonNode.get("message").asText());
        } else {
            throw new LogOnlyException(response.toString());
        }
    }

    @Override
    public JsonNode getStoreDetail() {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shop/all";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);

        Map<String, Object> body = new HashMap<>();
        body.put("offset", 0);
        body.put("limit", 1);
        body.put("client_phone", phoneNumber);

        APIRequest apiRequest = new APIRequest(
                url,
                HttpMethod.POST,
                headers,
                null,
                body
        );
        ResponseEntity<?> response = apiService.call(apiRequest);
        JsonNode jsonNode = Utils.convertToJson(response.getBody());
        HttpStatusCode status = response.getStatusCode();
        if (status == HttpStatus.OK) {
            return jsonNode.get("data").get("shops").get(0);
        } else if (status == HttpStatus.BAD_REQUEST) {
            System.out.println("call ghn api error: " + response.toString());
            throw new CustomException(jsonNode.get("message").asText());
        } else {
            throw new LogOnlyException(response.toString());
        }
    }

    @Override
    public JsonNode getOrderStatus(String orderCode) {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/detail";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);

        Map<String, Object> body = new HashMap<>();
        body.put("order_code", orderCode);

        APIRequest apiRequest = new APIRequest(
                url,
                HttpMethod.POST,
                headers,
                null,
                body
        );
        ResponseEntity<?> response = apiService.call(apiRequest);
        JsonNode jsonNode = Utils.convertToJson(response.getBody());
        HttpStatusCode status = response.getStatusCode();
        if (status == HttpStatus.OK) {
            System.out.println("jsonNode = " + jsonNode);
            return jsonNode.get("data").get("log");
        } else if (status == HttpStatus.BAD_REQUEST) {
            System.out.println("call ghn api error: " + response.toString());
            throw new CustomException(jsonNode.get("message").asText());
        } else {
            throw new LogOnlyException(response.toString());
        }
    }

    @Override
    public JsonNode calculateDeliveryTime(Long toDistrictId, String toWardCode, Long serviceId) {
        JsonNode store = getStoreDetail();

        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/leadtime";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);
        headers.set("ShopId", String.valueOf(shopId));

        Map<String, Object> body = new HashMap<>();
        body.put("from_district_id", store.get("district_id").asLong());
        body.put("from_ward_code", store.get("ward_code").asText());
        body.put("to_district_id", toDistrictId);
        body.put("to_ward_code", toWardCode);
        body.put("service_id", serviceId);

        APIRequest apiRequest = new APIRequest(
                url,
                HttpMethod.POST,
                headers,
                null,
                body
        );
        ResponseEntity<?> response = apiService.call(apiRequest);
        JsonNode jsonNode = Utils.convertToJson(response.getBody());
        HttpStatusCode status = response.getStatusCode();
        if (status == HttpStatus.OK) {
            return jsonNode.get("data");
        } else if (status == HttpStatus.BAD_REQUEST) {
            System.out.println("call ghn api error: " + response.toString());
            throw new CustomException(jsonNode.get("message").asText());
        } else {
            throw new LogOnlyException(response.toString());
        }
    }

    @Override
    public void cancelOrder(String orderCode) {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/switch-status/cancel";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);
        headers.set("ShopId", String.valueOf(shopId));

        Map<String, Object> body = new HashMap<>();
        List<String> orderCodeList = new ArrayList<>();
        orderCodeList.add(orderCode);
        body.put("order_codes", orderCodeList);

        APIRequest apiRequest = new APIRequest(
                url,
                HttpMethod.POST,
                headers,
                null,
                body
        );
        ResponseEntity<?> response = apiService.call(apiRequest);
        JsonNode jsonNode = Utils.convertToJson(response.getBody());
        HttpStatusCode status = response.getStatusCode();
        if (status == HttpStatus.OK) {
            return;
        } else if (status == HttpStatus.BAD_REQUEST) {
            System.out.println("call ghn api error: " + response.toString());
            throw new CustomException(jsonNode.get("message").asText());
        } else {
            throw new LogOnlyException(response.toString());
        }
    }

    @Override
    public String printOrder(String orderCode) {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/a5/gen-token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);

        Map<String, Object> body = new HashMap<>();
        List<String> orderCodeList = new ArrayList<>();
        orderCodeList.add(orderCode);
        body.put("order_codes", orderCodeList);

        APIRequest apiRequest = new APIRequest(
                url,
                HttpMethod.POST,
                headers,
                null,
                body
        );
        ResponseEntity<?> response = apiService.call(apiRequest);
        JsonNode jsonNode = Utils.convertToJson(response.getBody());
        HttpStatusCode status = response.getStatusCode();
        if (status == HttpStatus.OK) {
            return jsonNode.get("data").get("token").asText();
        } else if (status == HttpStatus.BAD_REQUEST) {
            System.out.println("call ghn api error: " + response.toString());
            throw new CustomException(jsonNode.get("message").asText());
        } else {
            throw new LogOnlyException(response.toString());
        }
    }
}
