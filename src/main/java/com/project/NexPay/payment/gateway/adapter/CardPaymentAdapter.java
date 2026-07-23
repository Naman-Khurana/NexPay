package com.project.NexPay.payment.gateway.adapter;

import com.project.NexPay.payment.gateway.PaymentAdapter;
import com.project.NexPay.payment.gateway.dto.PaymentRequest;
import com.project.NexPay.payment.gateway.dto.PaymentResult;

public class CardPaymentAdapter implements PaymentAdapter {
    @Override
    public PaymentResult initiate(PaymentRequest request) {
        return null;
    }
}
