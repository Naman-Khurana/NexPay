package com.project.NexPay.payment.processor.dto;

import com.project.NexPay.comman.entity.Money;
import com.project.NexPay.comman.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record PaymentProcessorRequest(
        UUID processingId,
        UUID paymentId,
        PaymentMethod method,
        Money amount,
        String pan,
        String expiry,
        Map<String,Object> methodDetails
) {

    public static PaymentProcessorRequest card(UUID paymentId,String pan, String expiry, Money money,Map<String, Object> methodDetails){
        return new PaymentProcessorRequest(
                UUID.randomUUID(),
                paymentId,
                PaymentMethod.NETBANKING,
                money,
                pan,
                expiry,
                methodDetails);
    }

    public static PaymentProcessorRequest nonCard(UUID paymentId, Money money,PaymentMethod paymentMethod,Map<String, Object> methodDetails){
        return new PaymentProcessorRequest(
                UUID.randomUUID(),
                paymentId,
                paymentMethod,
                money,
                null,
                null,
                methodDetails);
    }


}
