package com.project.NexPay.payment.gateway.dto;

import com.project.NexPay.comman.entity.Money;
import com.project.NexPay.comman.enums.PaymentMethod;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record PaymentRequest(
        UUID paymentId,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentMethod method,
        Map<String, Object> methodDetails
) {
}
