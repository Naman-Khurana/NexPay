package com.project.NexPay.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.NexPay.comman.entity.Money;
import com.project.NexPay.comman.enums.PaymentMethod;
import com.project.NexPay.comman.enums.PaymentStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record PaymentResponse(
        UUID id,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentStatus status,
        PaymentMethod method,
        Map<String, Object> methodDetails,
        String errorCode,
        String errorDescription,
        LocalDateTime capturedAt,
        LocalDateTime createdAt
) {
}
