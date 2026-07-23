package com.project.NexPay.payment.gateway;

import com.project.NexPay.comman.enums.PaymentMethod;
import com.project.NexPay.payment.config.PaymentAdapterConfig;
import com.project.NexPay.payment.gateway.dto.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRouter {

    private final Map<PaymentMethod,PaymentAdapter> paymentAdapters;

    public void initiate(PaymentRequest request){
        PaymentAdapter paymentAdapter=paymentAdapters.get(request.method());

        if(paymentAdapter==null)
            throw new IllegalArgumentException("No payment adapter registered for method : " + request.method());

        paymentAdapter.initiate(request);
    }

}
