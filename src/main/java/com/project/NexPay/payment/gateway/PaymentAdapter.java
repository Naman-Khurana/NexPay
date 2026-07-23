package com.project.NexPay.payment.gateway;

import com.project.NexPay.payment.gateway.dto.PaymentRequest;

public interface PaymentAdapter {
    void initiate(PaymentRequest request);
}
