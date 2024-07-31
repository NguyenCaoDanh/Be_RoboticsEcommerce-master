package com.wisdom.roboticsecommerce.Service;

import com.wisdom.roboticsecommerce.Dto.LinkPaymentResponse;
import com.wisdom.roboticsecommerce.Dto.VNPayRefund;
import com.wisdom.roboticsecommerce.Entities.Payment;

public interface PaymentService {
    LinkPaymentResponse linkPayment(Long orderId);
    Payment getAll(Double amount,Long orderId);
    void refundPayment(VNPayRefund vnPayRefund, String ipAddress);
}
