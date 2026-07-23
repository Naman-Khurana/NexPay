package com.project.NexPay.payment.processor.dto;

public sealed interface PaymentProcessorResponse permits
        PaymentProcessorResponse.Pending,
        PaymentProcessorResponse.Success,
        PaymentProcessorResponse.Failure
{

    record Pending(String processorReference) implements  PaymentProcessorResponse{ }

    record Success(String processorReference,String bankReference) implements  PaymentProcessorResponse{}

    record Failure(String error,String errorDescription) implements  PaymentProcessorResponse {}
}
