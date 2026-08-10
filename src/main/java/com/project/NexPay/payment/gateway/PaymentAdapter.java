package com.project.NexPay.payment.gateway;

import com.project.NexPay.payment.gateway.dto.PaymentRequest;
import com.project.NexPay.payment.gateway.dto.PaymentResult;

import java.util.UUID;

public interface PaymentAdapter {
    PaymentResult initiate(PaymentRequest request);

    PaymentResult capture(UUID paymentId);
}
