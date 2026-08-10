package com.project.NexPay.payment.service;

import com.project.NexPay.payment.dto.request.PaymentInitRequest;
import com.project.NexPay.payment.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse initiate(UUID merchantId, PaymentInitRequest request);

    PaymentResponse capture(UUID paymentId, UUID merchantId);
}
