package com.project.NexPay.payment.processor;

import com.project.NexPay.payment.processor.dto.PaymentProcessorRequest;
import com.project.NexPay.payment.processor.dto.PaymentProcessorResponse;

public interface PaymentProcessor {

    PaymentProcessorResponse charge(PaymentProcessorRequest request);

}
