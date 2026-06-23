package com.project.NexPay.payment.dto.response;

import com.project.NexPay.comman.entity.Money;
import com.project.NexPay.comman.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record OrderResponse (

        UUID id,

        UUID merchantId,

        String receipt,

        Money amount,

        OrderStatus status,

        Integer attempts,

        Map<String, Object> notes,

        LocalDateTime expiresAt,

        LocalDateTime createdAt

) {
}
