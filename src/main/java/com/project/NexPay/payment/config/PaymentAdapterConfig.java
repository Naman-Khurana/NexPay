package com.project.NexPay.payment.config;

import com.project.NexPay.comman.enums.PaymentMethod;
import com.project.NexPay.payment.gateway.PaymentAdapter;
import com.project.NexPay.payment.gateway.adapter.CardPaymentAdapter;
import com.project.NexPay.payment.gateway.adapter.NetBankingAdapter;
import com.project.NexPay.payment.gateway.adapter.UpiPaymentAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentAdapterConfig {

    private final NetBankingAdapter netBankingAdapter;
    private final UpiPaymentAdapter upiPaymentAdapter;
    private final CardPaymentAdapter cardPaymentAdapter;

    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymentAdapterMap() {
        return Map.of(
                PaymentMethod.CARD,cardPaymentAdapter,
                PaymentMethod.UPI,upiPaymentAdapter,
                PaymentMethod.NETBANKING,netBankingAdapter
        );
    }
}
