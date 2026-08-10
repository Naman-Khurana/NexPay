package com.project.NexPay.payment.gateway.adapter;

import com.project.NexPay.comman.Constants;
import com.project.NexPay.payment.gateway.PaymentAdapter;
import com.project.NexPay.payment.gateway.dto.PaymentRequest;
import com.project.NexPay.payment.gateway.dto.PaymentResult;

import java.util.UUID;

public class CardPaymentAdapter implements PaymentAdapter {
    @Override
    public PaymentResult initiate(PaymentRequest request) {
        return null;
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return new PaymentResult.Success(Constants.Card.CARD_REF);
    }
}
