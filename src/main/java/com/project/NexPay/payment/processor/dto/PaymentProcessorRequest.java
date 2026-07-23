package com.project.NexPay.payment.processor.dto;

import com.project.NexPay.comman.entity.Money;
import com.project.NexPay.comman.enums.PaymentMethod;

import java.util.Map;

public record PaymentProcessorRequest(
        PaymentMethod method,
        Money amount,
//        String pan,
//        String expiry,
        Map<String,Object> methodDetails
) {
}
