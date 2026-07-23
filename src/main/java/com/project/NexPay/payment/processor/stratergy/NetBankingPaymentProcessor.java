package com.project.NexPay.payment.processor.stratergy;

import com.project.NexPay.payment.processor.PaymentProcessor;
import com.project.NexPay.payment.processor.dto.PaymentProcessorRequest;
import com.project.NexPay.payment.processor.dto.PaymentProcessorResponse;

public class NetBankingPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        return null;
    }
}
