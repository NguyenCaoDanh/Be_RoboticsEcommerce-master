package com.wisdom.roboticsecommerce.Service.Impl;

import com.wisdom.roboticsecommerce.Config.ConfigPayment;
import com.wisdom.roboticsecommerce.Dto.LinkPaymentResponse;
import com.wisdom.roboticsecommerce.Dto.VNPayRefund;
import com.wisdom.roboticsecommerce.Entities.Order;
import com.wisdom.roboticsecommerce.Entities.Payment;
import com.wisdom.roboticsecommerce.Mapper.Mapper;
import com.wisdom.roboticsecommerce.Repositories.OrderRepository;
import com.wisdom.roboticsecommerce.Repositories.PaymentRepository;
import com.wisdom.roboticsecommerce.Service.PaymentService;
import com.wisdom.roboticsecommerce.Utils.Constants;
import com.wisdom.roboticsecommerce.Utils.Utility;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private Mapper mapper;
    @Override
    public LinkPaymentResponse linkPayment(Long orderId) {
        try {
            Optional<Order> orderOptional = orderRepository.findByIdAndDeleted(orderId, Constants.DONT_DELETE);
            if (orderOptional.isEmpty()){
                throw new MessageDescriptorFormatException("Không tìm thấy đơn hàng");
            }
            long amounts =  Math.round(orderOptional.get().getTotalPrice());
            String vnp_TxnRef = ConfigPayment.getRandomNumber(8);
            String vnp_Info = String.valueOf(orderId);
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_BankCode", "NCB");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_CurrCode","VND");
            vnp_Params.put("vnp_TmnCode",ConfigPayment.vnp_TmnCode);
            vnp_Params.put("vnp_Version", ConfigPayment.vnp_Version);
            vnp_Params.put("vnp_Command",ConfigPayment.vnp_Command);
            vnp_Params.put("vnp_OrderInfo",vnp_Info);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_Amount",String.valueOf(amounts*100));
            vnp_Params.put("vnp_ReturnUrl", ConfigPayment.vnp_ReturnUrl);
            vnp_Params.put("vnp_OrderType",ConfigPayment.vnp_OrderType);
            vnp_Params.put("vnp_IpAddr", ConfigPayment.getIpAddress());

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = ConfigPayment.hmacSHA512(ConfigPayment.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = ConfigPayment.vnp_PayUrl + "?" + queryUrl;
            LinkPaymentResponse linkPaymentResponse = new LinkPaymentResponse();
            linkPaymentResponse.setStatus("Success");
            linkPaymentResponse.setMessage("Thanh toán thành công!");
            linkPaymentResponse.setAmount(amounts);
            linkPaymentResponse.setUrl(paymentUrl);
            return linkPaymentResponse;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Payment getAll(Double amount, Long orderId) {
        try{
            Payment payment = new Payment();
            payment.setCreatAt(new Date());
            payment.setDeleted(Constants.DONT_DELETE);
            payment.setStatus(Constants.PAID);
            payment.setTotalPrice(amount);
            payment.setAccountId(mapper.getUserIdByToken());
            payment.setOrderId(orderId);
            paymentRepository.save(payment);
            return payment;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void refundPayment(VNPayRefund vnPayRefund, String ipAddress) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "refund";
        String vnp_TmnCode = ConfigPayment.vnp_TmnCode;
        String vnp_TxnRef = ConfigPayment.getRandomNumber(8);
//        String data = vnPayRefund.getVnPaymentId()
//                + "|" + vnp_Version
//                + "|" + vnp_Command
//                + "|" + vnp_TmnCode
//                + "|" + "02"
//                + "|" + vnp_TxnRef
//                + "|" + String.valueOf(vnPayment.getAmount()*100)
//                + "|" + vnPayment.getVnp_TransactionNo()
//                + "|" + vnPayment.getVnp_PayDate()
//                + "|" + vnPayRefund.getCreateBy()
//                + "|" + date
//                + "|" + ipAddress
//                + "|" + vnPayRefund.getOrderInfo();
//        String vnp_SecureHash = ConfigPayment.hmacSHA512(vnpaySecretKey, data);

        HashMap<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_RequestId", vnPayRefund.getVnPaymentId());
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_TransactionType", "02");
//        vnp_Params.put("vnp_TxnRef", vnPayment.getVnp_TxnRef());
//        vnp_Params.put("vnp_Amount", String.valueOf(vnPayment.getAmount()*100));
        vnp_Params.put("vnp_OrderInfo", vnPayRefund.getOrderInfo());
//        vnp_Params.put("vnp_TransactionNo", vnPayment.getVnp_TransactionNo());
//        vnp_Params.put("vnp_TransactionDate", vnPayment.getVnp_PayDate());
        vnp_Params.put("vnp_CreateBy", String.valueOf(vnPayRefund.getCreateBy()));
        vnp_Params.put("vnp_CreateDate", String.valueOf(LocalDateTime.now()));
        vnp_Params.put("vnp_IpAddr", ConfigPayment.getIpAddress());
//        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);

//        ApiCallResponse ApiCallResponse = new ApiCallResponse();
//        ApiCallResponse = iExternalAPIService.post(vnpayAPIUrl, null, vnp_Params);
//
//        if (ApiCallResponse.getStatus() != HttpStatus.OK) {
//            System.out.println("ApiCallResponse = " + ApiCallResponse.getBody());
//            throw new CustomException("There was an error during the refund process");
//        }

//        VNPayment vnPaymentRefund = new VNPayment();
//        HashMap<String, String> respone = new HashMap<>();
//        respone = (HashMap<String, String>) ApiCallResponse.getBody();
//
//        vnPaymentRefund.setVnp_BankCode(respone.get("vnp_BankCode"));
//        vnPaymentRefund.setVnp_TransactionNo(respone.get("vnp_TransactionNo"));
//        vnPaymentRefund.setVnp_TmnCode(respone.get("vnp_TmnCode"));
//        vnPaymentRefund.setVnp_TxnRef(respone.get("vnp_TxnRef"));
//        vnPaymentRefund.setVnp_OrderInfo(respone.get("vnp_OrderInfo"));
//        vnPaymentRefund.setVnp_Amount(respone.get("vnp_Amount"));
//        vnPaymentRefund.setVnp_ResponseCode(respone.get("vnp_ResponseCode"));
//        vnPaymentRefund.setVnp_ResponseId(respone.get("vnp_ResponseId"));
//        vnPaymentRefund.setVnp_Command(respone.get("vnp_Command"));
//        vnPaymentRefund.setVnp_PayDate(respone.get("vnp_PayDate"));
//        vnPaymentRefund.setVnp_TransactionType(respone.get("vnp_TransactionType"));
//        vnPaymentRefund.setVnp_SecureHash(respone.get("vnp_SecureHash"));
//        vnPaymentRefund.setVnp_TransactionStatus(respone.get("vnp_TransactionStatus"));
//        vnPaymentRefund.setVnp_Message(respone.get("vnp_Message"));
//        vnPaymentRefund.setName("Hoàn tiền cho khách hàng");
//        vnPaymentRefund.setDescription("");
//        vnPaymentRefund.setCreatedBy(vnPayRefund.getCreateBy());
//        vnPaymentRefund.setLastModifiedBy(vnPayRefund.getCreateBy());

//        if (!vnPaymentRefund.getVnp_ResponseCode().equals("00")) {
//            throw new CustomException(
//                    "There was an error during the refund process: "
//                            + vnPaymentRefund.getVnp_ResponseCode() + " "
//                            + vnPaymentRefund.getVnp_Message());
//        }

//        VNPayment vnPaymentRefundNew = new VNPayment();
//        vnPaymentRefundNew = create(vnPaymentRefund);

    }
}
