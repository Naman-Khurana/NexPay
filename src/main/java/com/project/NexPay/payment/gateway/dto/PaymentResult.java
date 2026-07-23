package com.project.NexPay.payment.gateway.dto;

public sealed interface PaymentResult permits
        PaymentResult.Failure,
        PaymentResult.Pending
{

    record Pending(String registrationReference) implements PaymentResult{}

    record Failure(String errorCode,String errorDescription) implements PaymentResult{}


}
