package com.project.NexPay.payment.config;

import com.project.NexPay.comman.enums.PaymentMethod;
import com.project.NexPay.payment.gateway.PaymentAdapter;
import com.project.NexPay.payment.gateway.adapter.CardPaymentAdapter;
import com.project.NexPay.payment.gateway.adapter.NetBankingAdapter;
import com.project.NexPay.payment.gateway.adapter.UpiPaymentAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentAdapterConfig {

    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymentAdapterMap() {
        return Map.of(
                PaymentMethod.CARD,new CardPaymentAdapter(),
                PaymentMethod.UPI,new UpiPaymentAdapter(),
                PaymentMethod.NETBANKING,new NetBankingAdapter()
        );
    }
}
